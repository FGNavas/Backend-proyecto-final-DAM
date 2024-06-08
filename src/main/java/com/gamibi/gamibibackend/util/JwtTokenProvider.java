package com.gamibi.gamibibackend.util;

import com.gamibi.gamibibackend.config.KeyGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Proveedor de tokens JWT para la autenticación y autorización.
 * <p>
 * Esta clase contiene métodos para generar, validar y extraer información de tokens JWT.
 */
@Component
public class JwtTokenProvider {
    private KeyGenerator keyGenerator = new KeyGenerator();

    private final SecretKey jwtSecretKey = Keys.hmacShaKeyFor(keyGenerator.generateKey().getBytes());
    private final long jwtExpirationInMs = 604800000; // 7 días

    /**
     * Genera un token JWT para un usuario dado.
     *
     * @param userDetails detalles del usuario para los que se genera el token
     * @return un token JWT como cadena
     */
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario contenido en el token
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * Valida un token JWT.
     *
     * @param authToken el token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            System.err.println("Error en la validacion del token");

        }
        return false;
    }
}


