package com.gamibi.gamibibackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para permitir CORS en la aplicación.
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    /**
     * Configura las políticas de CORS para permitir peticiones desde cualquier origen, método HTTP y cabecera.
     *
     * @param registry Registro de políticas de CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Permitir cualquier origen
                .allowedOrigins("*")
                // Permitir cualquier método HTTP
                .allowedMethods("*")
                // Permitir cualquier cabecera
                .allowedHeaders("*");
    }
}