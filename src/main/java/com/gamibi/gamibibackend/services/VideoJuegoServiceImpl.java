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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de videojuegos.
 */
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


    /**
     * Obtiene una lista de todos los videojuegos.
     *
     * @return lista de todos los videojuegos
     */
    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findAll() {
        return videoGameDao.findAll();
    }

    /**
     * Obtiene una lista paginada de todos los videojuegos.
     *
     * @param pageable información de paginación
     * @return página de videojuegos
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VideoJuego> findAll(Pageable pageable) {
        return videoGameDao.findAll(pageable);
    }

    /**
     * Busca un videojuego por su ID.
     *
     * @param id el ID del videojuego
     * @return el videojuego, o null si no se encuentra
     */
    @Override
    @Transactional(readOnly = true)
    public VideoJuego findById(Long id) {
        return videoGameDao.findById(id).orElse(null);
    }

    /**
     * Guarda un videojuego.
     *
     * @param videoGame el videojuego a guardar
     * @return el videojuego guardado
     */
    @Override
    @Transactional
    public VideoJuego save(VideoJuego videoGame) {
        return videoGameDao.save(videoGame);
    }

    /**
     * Elimina un videojuego por su ID.
     *
     * @param id el ID del videojuego a eliminar
     */
    @Override
    @Transactional(readOnly = true)
    public void delete(Long id) {
        videoGameDao.deleteById(id);
    }

    /**
     * Busca videojuegos por su nombre.
     *
     * @param titulo el título del videojuego
     * @return lista de videojuegos con el título especificado
     */
    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findByName(String titulo) {
        return videoGameDao.findByName(titulo);
    }

    /**
     * Busca videojuegos por su nombre y ID de usuario.
     *
     * @param titulo    el título del videojuego
     * @param usuarioId el ID del usuario
     * @return lista de videojuegos con el título especificado para el usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<VideoJuego> findByNameAndUserId(String titulo, Long usuarioId) {
        return videoGameDao.findByNameAndUserId(titulo, usuarioId);
    }

    /**
     * Busca la relación entre usuario y videojuego por sus IDs.
     *
     * @param userId       el ID del usuario
     * @param videoJuegoId el ID del videojuego
     * @return un Optional con la relación usuario-videojuego si existe
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserGame> findByUserIdAndVideoJuegoId(Long userId, Long videoJuegoId) {
        return videoGameDao.findByUserIdAndVideoJuegoId(userId, videoJuegoId);
    }

    /**
     * Obtiene la información de un videojuego desde la API externa.
     *
     * @param id el ID del videojuego en la API externa
     * @return la información del videojuego en formato VideoGameRAWGDTO
     */
    @Override
    @Transactional(readOnly = true)
    public VideoGameRAWGDTO getGameInfoFromApi(String id) {
        return convertirJsonAGameInfo(rawgAPIService.getGameInfoById(id));
    }

    /**
     * Obtiene un DTO de videojuego con información del usuario.
     *
     * @param titulo el título del videojuego
     * @param userId el ID del usuario
     * @return un VideoGameDTO con la información del videojuego y del usuario
     */
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

    /**
     * Busca los juegos favoritos de un usuario por su ID.
     *
     * @param userId el ID del usuario
     * @return lista de VideoGameDTO con los juegos favoritos del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<VideoGameDTO> findFavoriteGamesByUserId(Long userId) {
        List<UserGame> userGames = userGameDao.findByUsuario_IdAndFavoriteTrue(userId);
        return userGames.stream().map(userGame -> {
            VideoGameRAWGDTO rawgVideoGame = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(userGame.getVideoJuego().getId())));
            return new VideoGameDTO(rawgVideoGame, userGame.getPurchaseDate(), userGame.isFavorite(), userGame.getStatus());
        }).collect(Collectors.toList());
    }

    /**
     * Busca todos los juegos de un usuario por su ID.
     *
     * @param userId el ID del usuario
     * @return lista de VideoGameDTO con todos los juegos del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<VideoGameDTO> findAllGamesByUserId(Long userId) {
        List<VideoGameDTO> listaJuegos = new ArrayList<>();
        List<UserGame> userGames = videoGameDao.findAllGamesByUserId(userId);
        userGames.forEach(gameUsers -> {

            VideoGameRAWGDTO videoGameRAWGDTO;
            videoGameRAWGDTO = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(gameUsers.getUsuario().getId().toString()));
            VideoGameDTO videoGameDTO = new VideoGameDTO(videoGameRAWGDTO, gameUsers.getPurchaseDate(), gameUsers.isFavorite(), gameUsers.getStatus());
            listaJuegos.add(videoGameDTO);
        });
        return listaJuegos;
    }

    /**
     * Añade un juego a la lista de un usuario.
     *
     * @param userId       el ID del usuario
     * @param gameId       el ID del videojuego
     * @param purchaseDate la fecha de compra del juego
     * @param favorite     si el juego es favorito o no
     * @param status       el estado del juego
     * @param rating       la calificación del juego
     */
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

    /**
     * Actualiza el estado de favorito de un juego para un usuario.
     *
     * @param userId   el ID del usuario
     * @param gameId   el ID del videojuego
     * @param favorite true si el juego es favorito, false si no lo es
     */
    @Override
    public void updateFavoriteStatus(Long userId, Long gameId, boolean favorite) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGame.setFavorite(favorite);
        userGameRepository.save(userGame);
    }

    /**
     * Elimina un juego de la lista de un usuario.
     *
     * @param userId el ID del usuario
     * @param gameId el ID del videojuego
     */
    @Override
    public void removeGameFromUser(Long userId, Long gameId) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGameRepository.delete(userGame);
    }

    /**
     * Actualiza el estado de un juego para un usuario.
     *
     * @param userId el ID del usuario
     * @param gameId el ID del videojuego
     * @param status el nuevo estado del juego
     */
    @Override
    public void updateGameStatus(Long userId, Long gameId, GameStatus status) {
        UserGame userGame = userGameRepository.findByUsuario_IdAndVideoJuego_Id(userId, gameId)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado en la lista del usuario"));
        userGame.setStatus(status);
        userGameRepository.save(userGame);
    }

    /**
     * Busca los juegos pendientes de un usuario por su ID.
     *
     * @param userId el ID del usuario
     * @return lista de VideoGameDTO con los juegos pendientes del usuario
     */
    @Override
    public List<VideoGameDTO> findPendingGamesByUserId(Long userId) {
        List<UserGame> pendingUserGames = userGameRepository.findByUsuarioIdAndStatus(userId, GameStatus.PENDIENTE);
        List<VideoGameDTO> userPendingList = new ArrayList<>();

        pendingUserGames.forEach(pug -> {
            VideoGameRAWGDTO videoGameRAWGDTO = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(pug.getVideoJuego().getId())));
            VideoGameDTO videoGameDTO = new VideoGameDTO(videoGameRAWGDTO, pug.getPurchaseDate(), pug.isFavorite(), pug.getStatus());
            userPendingList.add(videoGameDTO);
        });

        return userPendingList;
    }

    /**
     * Convierte la información del juego desde formato JSON a un objeto VideoGameRAWGDTO.
     *
     * @param gameInfoJson la información del juego en formato JSON
     * @return un objeto VideoGameRAWGDTO con la información del juego
     */


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
