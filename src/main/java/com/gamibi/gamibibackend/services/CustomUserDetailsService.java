package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.entity.Usuario;
import com.gamibi.gamibibackend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Servicio personalizado para la carga de detalles de usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carga los detalles del usuario basados en su nombre de usuario (email).
     *
     * @param username El nombre de usuario (email) del usuario.
     * @return Los detalles del usuario.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el email especificado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + username));
        return new org.springframework.security.core.userdetails.User(usuario.getEmail(), usuario.getPassword(), new ArrayList<>());
    }

    /**
     * Obtiene un usuario por su dirección de email.
     *
     * @param email La dirección de email del usuario.
     * @return El usuario encontrado.
     * @throws UsernameNotFoundException Si no se encuentra ningún usuario con el email especificado.
     */
    public Usuario getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + email));
    }
}
