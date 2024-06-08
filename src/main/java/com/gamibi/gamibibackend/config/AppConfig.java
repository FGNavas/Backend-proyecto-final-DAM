package com.gamibi.gamibibackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de la aplicación.
 */
@Configuration
public class AppConfig {
    /**
     * Bean para proporcionar un RestTemplate.
     *
     * @return RestTemplate para realizar solicitudes HTTP.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}