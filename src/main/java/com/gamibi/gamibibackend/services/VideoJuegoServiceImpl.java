package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.dao.IUserGameDao;
import com.gamibi.gamibibackend.dao.IVideoGameDao;
import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.Usuario;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameRAWGDTO;
import com.gamibi.gamibibackend.repository.UserGameRepository;
import com.gamibi.gamibibackend.repository.UsuarioRepository;
import com.google.gson.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class VideoJuegoServiceImpl implements IVideoJuegoService {

    @Autowired
    private IVideoGameDao videoGameDao;
    @Autowired
    private IUserGameDao userGameDao;
    @Autowired
    private RawgAPIService rawgAPIService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UserGameRepository userGameRepository;
    private JsonObject jsonObject;

    private List<VideoGameDTO> listaJuegosUsuario = new ArrayList<>();
    private List<VideoGameRAWGDTO> listaInfoJuegos = new ArrayList<>();
    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findAll() {
        return videoGameDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VideoJuego> findAll(Pageable pageable) {
        return videoGameDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoJuego findById(Long id) {
        return videoGameDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public VideoJuego save(VideoJuego videoGame) {
        return videoGameDao.save(videoGame);
    }

    @Override
    @Transactional(readOnly = true)
    public void delete(Long id) {
        videoGameDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findByName(String titulo) {
        return videoGameDao.findByName(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findByNameAndUserId(String titulo, Long usuarioId) {
        return videoGameDao.findByNameAndUserId(titulo, usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserGame> findByUserIdAndVideoJuegoId(Long userId, Long videoJuegoId) {
        return videoGameDao.findByUserIdAndVideoJuegoId(userId, videoJuegoId);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoGameRAWGDTO getGameInfoFromApi(String id) {
        return convertirJsonAGameInfo(rawgAPIService.getGameInfoById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public VideoGameDTO getVideoGameDTO(String titulo, Long userId) {
        // Buscar el videojuego en la BBDD
        List<VideoJuego> videoJuegos = videoGameDao.findByName(titulo);
        if (videoJuegos.isEmpty()) {
            return null;
        }

        VideoJuego videoJuego = videoJuegos.get(0); // Tomamos el primer resultado
        Long videoJuegoId = videoJuego.getId();

        // Consultar a la API de RAWG con el ID del videojuego
        VideoGameRAWGDTO rawgVideoGame = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(videoJuegoId)));

        // Verificar si el usuario tiene el juego en su lista
        Optional<UserGame> userGameOpt = videoGameDao.findByUserIdAndVideoJuegoId(userId, videoJuegoId);

        VideoGameDTO videoGameDTO = new VideoGameDTO();
        videoGameDTO.setRawgVideoGame(rawgVideoGame);

        if (userGameOpt.isPresent()) {
            UserGame userGame = userGameOpt.get();
            videoGameDTO.setPurchaseDate(userGame.getPurchaseDate());
            videoGameDTO.setFavorite(userGame.isFavorite());
            videoGameDTO.setStatus(userGame.getStatus());
        }

        return videoGameDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VideoGameDTO> findFavoriteGamesByUserId(Long userId) {
        List<UserGame> userGames = userGameDao.findByUsuario_IdAndFavoriteTrue(userId);
        return userGames.stream().map(userGame -> {
            VideoGameRAWGDTO rawgVideoGame = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(userGame.getVideoJuego().getId())));
            return new VideoGameDTO(rawgVideoGame, userGame.getPurchaseDate(), userGame.isFavorite(), userGame.getStatus());
        }).collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public List<VideoGameDTO> findAllGamesByUserId(Long userId) {
        List<VideoGameDTO> listaJuegos = new ArrayList<>();
        List<UserGame>  userGames = videoGameDao.findAllGamesByUserId(userId);
        userGames.forEach(gameUsers ->{

            VideoGameRAWGDTO videoGameRAWGDTO;
            videoGameRAWGDTO= convertirJsonAGameInfo(rawgAPIService.getGameInfoById(gameUsers.getUsuario().getId().toString()));
            VideoGameDTO videoGameDTO = new VideoGameDTO(videoGameRAWGDTO,gameUsers.getPurchaseDate(),gameUsers.isFavorite(),gameUsers.getStatus());
            listaJuegos.add(videoGameDTO);
        });
        return listaJuegos;
    }

    @Override
    public void addGameToUser(Long userId, Long gameId, Date purchaseDate, boolean favorite, GameStatus status, int rating) {
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserGame userGame = new UserGame();
        userGame.setUsuario(usuario);
        userGame.setVideoJuego(new VideoJuego(gameId, null));
        userGame.setPurchaseDate(purchaseDate);
        userGame.setFavorite(favorite);
        userGame.setStatus(status);
        userGame.setRating(rating);

        userGameRepository.save(userGame);
    }

    @Override
    public void updateFavoriteStatus(Long userId, Long gameId, boolean favorite) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGame.setFavorite(favorite);
        userGameRepository.save(userGame);
    }

    @Override
    public void removeGameFromUser(Long userId, Long gameId) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGameRepository.delete(userGame);
    }

    @Override
    public void updateGameStatus(Long userId, Long gameId, GameStatus status) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGame.setStatus(status);
        userGameRepository.save(userGame);
    }

    @Override
    public List<VideoGameDTO> findPendingGamesByUserId(Long userId) {
        List<UserGame> pendingUserGames = userGameRepository.findByUsuarioIdAndStatus(userId, GameStatus.PENDIENTE);
        List<VideoGameDTO> userPendingList = new ArrayList<>();

        pendingUserGames.forEach(pug ->{
            VideoGameRAWGDTO videoGameRAWGDTO = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(pug.getVideoJuego().getId())));
            VideoGameDTO videoGameDTO = new VideoGameDTO(videoGameRAWGDTO,pug.getPurchaseDate(),pug.isFavorite(),pug.getStatus());
            userPendingList.add(videoGameDTO);
        });

        return userPendingList;
    }



        private VideoGameRAWGDTO convertirJsonAGameInfo(String gameInfoJson) {

        JsonElement jsonElement = JsonParser.parseString(gameInfoJson);
        jsonObject = jsonElement.getAsJsonObject();

        VideoGameRAWGDTO videoGameRAWGDTO = new VideoGameRAWGDTO();

        if (jsonObject.has("id")) {
            JsonElement descripcionElement = jsonObject.get("id");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setId(jsonObject.get("id").getAsLong());
            } else {
                videoGameRAWGDTO.setId(1L);
            }
        }

        if (jsonObject.has("name")) {
            JsonElement descripcionElement = jsonObject.get("name");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setName(jsonObject.get("name").getAsString());
            } else {
                videoGameRAWGDTO.setName("No name");
            }
        }

        if (jsonObject.has("description_raw")) {
            JsonElement descripcionElement = jsonObject.get("description_raw");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setDescripcion_raw(jsonObject.get("description_raw").getAsString());
            } else {
                videoGameRAWGDTO.setDescripcion_raw("{ No description }");
            }
        }
        if (jsonObject.has("metacritic")) {
            JsonElement descripcionElement = jsonObject.get("metacritic");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setMetacritic(jsonObject.get("metacritic").getAsDouble());
            } else {
                videoGameRAWGDTO.setMetacritic(0.0);
            }
        }
        if (jsonObject.has("rating")) {
            JsonElement descripcionElement = jsonObject.get("rating");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setRating(jsonObject.get("rating").getAsDouble());
            } else {
                videoGameRAWGDTO.setRating(0.0);
            }
        }
        if (jsonObject.has("rating_top")) {
            JsonElement descripcionElement = jsonObject.get("rating_top");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setRating_top(jsonObject.get("rating_top").getAsDouble());
            } else {
                videoGameRAWGDTO.setRating_top(0.0);
            }
        }
        if (jsonObject.has("released")) {
            JsonElement descripcionElement = jsonObject.get("released");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setReleased(jsonObject.get("released").getAsString());
            } else {
                videoGameRAWGDTO.setReleased("");
            }
        }
        if (jsonObject.has("playtime")) {
            JsonElement descripcionElement = jsonObject.get("playtime");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setPlaytime(jsonObject.get("playtime").getAsDouble());
            } else {
                videoGameRAWGDTO.setPlaytime(0.0);
            }
        }
        if (jsonObject.has("background_image")) {
            JsonElement descripcionElement = jsonObject.get("background_image");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setBackground_image(jsonObject.get("background_image").getAsString());
            } else {
                videoGameRAWGDTO.setBackground_image("");
            }
        }
        if (jsonObject.has("background_image_additional")) {
            JsonElement descripcionElement = jsonObject.get("background_image_additional");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setBackground_image_additional(jsonObject.get("background_image_additional").getAsString());
            } else {
                videoGameRAWGDTO.setBackground_image_additional("");
            }
        }
        if (jsonObject.has("saturated_color")) {
            JsonElement descripcionElement = jsonObject.get("saturated_color");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setSaturated_color(jsonObject.get("saturated_color").getAsString());
            } else {
                videoGameRAWGDTO.setSaturated_color("");
            }
        }
        if (jsonObject.has("dominant_color")) {
            JsonElement descripcionElement = jsonObject.get("dominant_color");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setDominant_color(jsonObject.get("dominant_color").getAsString());
            } else {
                videoGameRAWGDTO.setDominant_color("");
            }
        }
        if (jsonObject.has("website")) {
            JsonElement descripcionElement = jsonObject.get("website");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWGDTO.setWebsite(jsonObject.get("website").getAsString());
            } else {
                videoGameRAWGDTO.setWebsite("");
            }
        }
        if (jsonObject.has("genres")) {
            JsonArray genresArray = jsonObject.getAsJsonArray("genres");
            List<String> genreNames = new ArrayList<>();
            genresArray.forEach(genreElement -> {
                if (!genreElement.isJsonNull()) {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    JsonElement nameElement = genreObject.get("name");
                    if (!nameElement.isJsonNull()) {
                        String genreName = nameElement.getAsString();
                        genreNames.add(genreName);
                    } else {
                        genreNames.add("Sin genero");
                    }
                }
            });
            videoGameRAWGDTO.setGenres(genreNames);
        }
        if (jsonObject.has("publishers")) {
            JsonArray publishersArray = jsonObject.getAsJsonArray("publishers");
            List<String> publisherNames = new ArrayList<>();
            publishersArray.forEach(genreElement -> {
                if (!genreElement.isJsonNull()) {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    JsonElement nameElement = genreObject.get("name");
                    if (!nameElement.isJsonNull()) {
                        String genreName = nameElement.getAsString();
                        publisherNames.add(genreName);
                    } else {
                        publisherNames.add("Sin publisher");
                    }
                }
            });
            videoGameRAWGDTO.setPublishers(publisherNames);
        }
        if (jsonObject.has("platforms")) {
            JsonArray platformsArray = jsonObject.getAsJsonArray("platforms");
            List<String> platformNames = new ArrayList<>();
            for (JsonElement platformElement : platformsArray) {
                JsonObject platformObject = platformElement.getAsJsonObject().getAsJsonObject("platform");
                if (platformObject.has("name")) {
                    String nameElement = platformObject.get("name").getAsString();
                    platformNames.add(nameElement);
                } else {
                    platformNames.add("Unknown Platform");
                }
            }
            videoGameRAWGDTO.setPlatforms(platformNames);
        }


        return videoGameRAWGDTO;
    }
}
