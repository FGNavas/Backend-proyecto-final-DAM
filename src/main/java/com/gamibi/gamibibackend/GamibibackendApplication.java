package com.gamibi.gamibibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada para la aplicación Gamibi Backend.
 * <p>
 * Esta clase inicializa y ejecuta la aplicación Spring Boot.
 */
@SpringBootApplication
@EnableScheduling
public class GamibibackendApplication {
    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos pasados a la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(GamibibackendApplication.class, args);

/*		// Generador de claves
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "123456";
		String encodedPassword = encoder.encode(rawPassword);
*/
        // System.out.println(encodedPassword);
    }

}
