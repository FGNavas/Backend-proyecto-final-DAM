package com.gamibi.gamibibackend.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * Clase para generar claves de seguridad.
 */
public class KeyGenerator {
    /**
     * Método para generar una clave secreta y codificarla en Base64.
     *
     * @return Clave secreta codificada en Base64.
     */
    public String generateKey() {
        // Generar una clave usando el algoritmo HS512
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        byte[] encodedKey = key.getEncoded();
        // Codificar la clave en Base64
        String base64EncodedKey = Base64.getEncoder().encodeToString(encodedKey);
        // Retornar la clave codificada
        return base64EncodedKey;

    }


}
