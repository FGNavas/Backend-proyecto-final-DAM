package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.dao.IUserGameDao;
import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.ResponseDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameRAWGDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameSearchResultDTO;
import com.gamibi.gamibibackend.repository.UserGameRepository;
import com.gamibi.gamibibackend.repository.VideoJuegoRepository;
import com.gamibi.gamibibackend.services.RawgAPIService;
import com.gamibi.gamibibackend.services.VideoJuegoServiceImpl;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las solicitudes relacionadas con los juegos.
 */
@RestController
@RequestMapping("/gamibiApi")
public class GameController {

    private final RawgAPIService rawgAPIService;
    private final VideoJuegoRepository videoJuegoRepository;
    @Autowired
    private IUserGameDao userGameDao;
    private final UserGameRepository userGameRepository;
    private final VideoJuegoServiceImpl videoJuegoService;

    private JsonObject jsonObject;

    /**
     * Constructor de GameController.
     *
     * @param rawgAPIService       Servicio para interactuar con la API de RAWG.
     * @param videoJuegoRepository Repositorio para acceder a los datos de los juegos.
     * @param userGameRepository   Repositorio para acceder a los datos de los juegos de usuario.
     * @param videoJuegoService    Servicio para operaciones relacionadas con los juegos.
     */
    public GameController(RawgAPIService rawgAPIService, VideoJuegoRepository videoJuegoRepository, UserGameRepository userGameRepository, VideoJuegoServiceImpl videoJuegoService) {
        this.rawgAPIService = rawgAPIService;
        this.videoJuegoRepository = videoJuegoRepository;
        this.userGameRepository = userGameRepository;
        this.videoJuegoService = videoJuegoService;
    }

    /**
     * Obtiene información detallada de un juego para un usuario específico, mediante la Id asignada.
     *
     * @param gameId ID del juego.
     * @param userId ID del usuario.
     * @return ResponseEntity que contiene la información del juego.
     */
    @GetMapping("/games/{gameId}")
    public ResponseEntity<?> getGameInfo(@PathVariable Long gameId, @PathVariable Long userId) {
        // Buscar información del UserGame en la base de datos
        Optional<UserGame> optionalUserGame = userGameDao.findByUsuario_IdAndVideoJuego_Id(userId, gameId);

        // Buscar información del juego en la API de RAWG
        String gameInfoJson = rawgAPIService.getGameInfoById(String.valueOf(gameId));
        VideoGameRAWGDTO rawgVideoGameDTO = convertirJsonAGameInfo(gameInfoJson);

        if (optionalUserGame.isPresent()) {
            // Si el juego está en la base de datos, crear un VideoGameDTO combinando la información
            UserGame userGame = optionalUserGame.get();
            VideoGameDTO videoGameDTO = new VideoGameDTO();
            videoGameDTO.setRawgVideoGame(rawgVideoGameDTO);
            videoGameDTO.setPurchaseDate(userGame.getPurchaseDate());
            videoGameDTO.setFavorite(userGame.isFavorite());
            videoGameDTO.setStatus(userGame.getStatus());

            return ResponseEntity.ok().body(new ResponseDTO("VideoGameDTO", videoGameDTO));
        } else {
            // Si el juego no está en la base de datos, responder con el VideoGameRAWGDTO
            return ResponseEntity.ok().body(new ResponseDTO("VideoGameRAWGDTO", rawgVideoGameDTO));
        }
    }

    /**
     * Obtiene los juegos favoritos de un usuario.
     *
     * @param userId ID del usuario.
     * @return ResponseEntity que contiene la lista de juegos favoritos.
     */
    @GetMapping("/games/favorites")
    public ResponseEntity<?> getFavoriteGames(@RequestParam Long userId) {
        List<VideoGameDTO> favoriteGames = videoJuegoService.findFavoriteGamesByUserId(userId);
        return new ResponseEntity<>(favoriteGames, HttpStatus.OK);
    }

    /**
     * Obtiene todos los juegos de un usuario.
     *
     * @param userId ID del usuario.
     * @return ResponseEntity que contiene la lista de todos los juegos del usuario.
     */
    @GetMapping("/games/user")
    public ResponseEntity<?> getUserGames(@RequestParam Long userId) {
        List<VideoGameDTO> userGames = videoJuegoService.findAllGamesByUserId(userId);
        return new ResponseEntity<>(userGames, HttpStatus.OK);
    }

    /**
     * Busca juegos por su nombre.
     *
     * @param titulo Nombre del juego a buscar.
     * @return ResponseEntity que contiene la lista de juegos encontrados.
     */
    @GetMapping("/games/search/byname")
    public ResponseEntity<?> getGamesByName(@RequestParam String titulo) {

        List<VideoJuego> listaJuegos = videoJuegoService.findByName(titulo);
        List<VideoGameRAWGDTO> rawgGames = new ArrayList<>();
        listaJuegos.forEach(j -> {
            VideoGameRAWGDTO juego = convertirJsonAGameInfo(rawgAPIService.getGameInfoById(String.valueOf(j.getId())));
            if (rawgGames != null) {
                rawgGames.add(juego);
            }
        });

        // Convertir la información en una lista de VideoGameDTO
        List<VideoGameDTO> games = rawgGames.stream()
                .map(rawgGame -> new VideoGameDTO(rawgGame, null, false, null)) // nulls para PurchaseDate, favorite y status
                .collect(Collectors.toList());

        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    /**
     * Agrega un juego a la lista de juegos de un usuario.
     *
     * @param userId       ID del usuario.
     * @param gameId       ID del juego.
     * @param purchaseDate Fecha de compra del juego.
     * @param favorite     Indicador de juego favorito.
     * @param status       Estado del juego.
     * @param rating       Calificación del juego.
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/user/{userId}/game/{gameId}")
    public ResponseEntity<Void> addGameToUser(@PathVariable Long userId, @PathVariable Long gameId,
                                              @RequestParam Date purchaseDate, @RequestParam boolean favorite,
                                              @RequestParam GameStatus status, @RequestParam int rating) {
        videoJuegoService.addGameToUser(userId, gameId, purchaseDate, favorite, status, rating);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Actualiza el estado de favorito de un juego para un usuario.
     *
     * @param userId   ID del usuario.
     * @param gameId   ID del juego.
     * @param favorite Indicador de juego favorito.
     * @return ResponseEntity con el estado de la operación.
     */
    @PutMapping("/user/{userId}/game/{gameId}/favorite")
    public ResponseEntity<Void> updateFavoriteStatus(@PathVariable Long userId, @PathVariable Long gameId,
                                                     @RequestParam boolean favorite) {
        videoJuegoService.updateFavoriteStatus(userId, gameId, favorite);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Elimina un juego de la lista de juegos de un usuario.
     *
     * @param userId ID del usuario.
     * @param gameId ID del juego.
     * @return ResponseEntity con el estado de la operación.
     */
    @DeleteMapping("/user/{userId}/game/{gameId}")
    public ResponseEntity<Void> removeGameFromUser(@PathVariable Long userId, @PathVariable Long gameId) {
        videoJuegoService.removeGameFromUser(userId, gameId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Actualiza el estado de un juego para un usuario.
     *
     * @param userId ID del usuario.
     * @param gameId ID del juego.
     * @param status Nuevo estado del juego.
     * @return ResponseEntity con el estado de la operación.
     */
    @PutMapping("/user/{userId}/game/{gameId}/status")
    public ResponseEntity<Void> updateGameStatus(@PathVariable Long userId, @PathVariable Long gameId,
                                                 @RequestParam GameStatus status) {
        videoJuegoService.updateGameStatus(userId, gameId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Convierte la información de un juego en formato JSON a un objeto VideoGameRAWGDTO.
     *
     * @param gameInfoJson Información del juego en formato JSON.
     * @return Objeto VideoGameRAWGDTO.
     */

    private VideoGameRAWGDTO convertirJsonAGameInfo(String gameInfoJson) {
        Gson gson = new Gson();
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


    @GetMapping("/search")
    public ResponseEntity<List<VideoGameSearchResultDTO>> searchGames(@RequestParam String name, @RequestParam int page, @RequestParam int size) {
        List<VideoJuego> videoJuegos = videoJuegoRepository.findByTituloContainingIgnoreCase(name);
        if (videoJuegos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
        List<VideoGameSearchResultDTO> searchResults = new ArrayList<>();
        int start = page * size;
        int end = Math.min(start + size, videoJuegos.size());

        for (int i = start; i < end; i++) {
            VideoJuego videoJuego = videoJuegos.get(i);
            String gameInfoJson = rawgAPIService.getGameInfoById(videoJuego.getId().toString());
            VideoGameRAWGDTO gameInfo = convertirJsonAGameInfo(gameInfoJson);

            VideoGameSearchResultDTO resultDTO = new VideoGameSearchResultDTO(
                    videoJuego.getId(),
                    videoJuego.getTitulo(),
                    gameInfo.getBackground_image()
            );
            searchResults.add(resultDTO);
        }

        return ResponseEntity.ok().body(searchResults);
    }
}

