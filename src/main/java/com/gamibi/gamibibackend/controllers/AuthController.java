package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.entity.JwtResponse;
import com.gamibi.gamibibackend.entity.Usuario;
import com.gamibi.gamibibackend.entityDTO.LoginRequest;
import com.gamibi.gamibibackend.services.CustomUserDetailsService;
import com.gamibi.gamibibackend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para autenticar usuarios.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * @param loginRequest Datos de inicio de sesión del usuario.
     * @return ResponseEntity que contiene el token JWT y el ID del usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            String token = jwtTokenProvider.generateToken(userDetails);

            // Obtener el usuario y su ID
            Usuario user = userDetailsService.getUserByEmail(loginRequest.getEmail());
            Long userId = user.getId();

            return ResponseEntity.ok(new JwtResponse(token, userId));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
