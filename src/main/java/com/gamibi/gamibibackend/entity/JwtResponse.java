package com.gamibi.gamibibackend.entity;

/**
 * Representa la respuesta JWT que contiene el token de autenticación y el ID de usuario.
 */
public class JwtResponse {

    private String token;
    private Long userId;

    /**
     * Constructor de JwtResponse.
     *
     * @param token  Token de autenticación JWT.
     * @param userId ID del usuario asociado al token.
     */
    public JwtResponse(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    /**
     * Obtiene el token de autenticación.
     *
     * @return El token de autenticación JWT.
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token de autenticación.
     *
     * @param token El token de autenticación JWT.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene el ID de usuario.
     *
     * @return El ID del usuario.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Establece el ID de usuario.
     *
     * @param userId El ID del usuario.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

