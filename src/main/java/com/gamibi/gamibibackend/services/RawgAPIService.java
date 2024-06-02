package com.gamibi.gamibibackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        String url = rawgAPIURL + gameId + "?key=" + rawgAPIKey;
        return restTemplate.getForObject(url, String.class);
    }
}