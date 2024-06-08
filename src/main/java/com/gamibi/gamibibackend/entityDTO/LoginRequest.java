package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para manejar las solicitudes de inicio de sesión.
 */

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;


}


