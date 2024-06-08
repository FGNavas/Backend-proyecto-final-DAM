package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.entity.ApiRequestState;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.repository.ApiRequestStateRepository;
import com.gamibi.gamibibackend.repository.VideoJuegoRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Servicio para recuperar datos de la API y almacenarlos en la base de datos.
 */
@Service
public class ApiDataFetcherService {

    @Autowired
    private RawgAPIService rawgAPIService;

    @Autowired
    private VideoJuegoRepository videoJuegoRepository;

    @Autowired
    private ApiRequestStateRepository apiRequestStateRepository;

    /**
     * Recupera datos de la API y los almacena en la base de datos.
     */
    @Transactional
    public void fetchAndStoreData() {
        // Obtener el último estado de la petición o inicializarlo
        Optional<ApiRequestState> optionalState = apiRequestStateRepository.findById(1L);
        ApiRequestState requestState = optionalState.orElseGet(() -> {
            ApiRequestState newState = new ApiRequestState();
            newState.setLastGameId(0L);
            return newState;
        });

        Long currentGameId = requestState.getLastGameId();
        int requestsPerDay = 100;

        for (int i = 0; i < requestsPerDay; i++) {
            currentGameId++;

            // Hacer una petición a la API por ID
            String gameInfoJson = rawgAPIService.getGameInfoById(String.valueOf(currentGameId));
            if (gameInfoJson == null) {
                continue;
            }
            VideoJuego game = parseApiResponse(gameInfoJson, currentGameId);

            // Guardar el juego en la base de datos si no es null
            if (game != null) {
                System.out.println(game.getId() + " " + game.getTitulo());
                videoJuegoRepository.save(game);
            }
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        // Actualizar y guardar el estado de la petición
        requestState.setLastGameId(currentGameId);
        apiRequestStateRepository.save(requestState);
    }

    /**
     * Parsea la respuesta de la API y crea una instancia de VideoJuego.
     *
     * @param response La respuesta de la API.
     * @param gameId   El ID del juego.
     * @return El juego parseado o null si no se pudo parsear.
     */
     
    private VideoJuego parseApiResponse(String response, Long gameId) {
        // Parsear la respuesta de la API y crear una instancia de VideoJuego
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        if (jsonObject.has("id") && jsonObject.has("name")) {
            Long id = jsonObject.get("id").getAsLong();
            String title = jsonObject.get("name").getAsString();
            return new VideoJuego(id, title);
        }

        return null;
    }
}