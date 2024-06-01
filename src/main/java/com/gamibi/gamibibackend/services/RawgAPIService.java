package com.gamibi.gamibibackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RawgAPIService {

    @Value("${rawg.api.key}") // Asegúrate de tener esta propiedad en tu archivo application.properties
    private String rawgAPIKey;

    private final RestTemplate restTemplate;

    public RawgAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getGameInfoById(String gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "?key=" + rawgAPIKey;
        return restTemplate.getForObject(url, String.class);
    }
}