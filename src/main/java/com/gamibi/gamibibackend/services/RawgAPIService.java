package com.gamibi.gamibibackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RawgAPIService {

    @Value("${rawg.api.key}")
    private String rawgAPIKey;
    @Value("${rawg.api.url}")
    private String rawgAPIURL;
    private final RestTemplate restTemplate;

    public RawgAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGameInfoById(String gameId) {
        try {
            String url = rawgAPIURL + gameId + "?key=" + rawgAPIKey;
            return restTemplate.getForObject(url, String.class);
        } catch (HttpClientErrorException e) {
            // Manejar el error 404 (Not Found) aquí
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("El juego con ID " + gameId + " no fue encontrado en la API RAWG.");
            } else {
                // Manejar otros errores de cliente (4xx)
                System.out.println("Error al obtener información del juego con ID " + gameId + ": " + e.getMessage());
            }
        } catch (HttpServerErrorException e) {
            // Manejar errores del servidor (5xx)
            System.out.println("Error interno del servidor al obtener información del juego con ID " + gameId + ": " + e.getMessage());
        } catch (RestClientException e) {
            // Manejar otros errores de cliente RestTemplate
            System.out.println("Error de cliente al obtener información del juego con ID " + gameId + ": " + e.getMessage());
        }
        return null;
    }
}