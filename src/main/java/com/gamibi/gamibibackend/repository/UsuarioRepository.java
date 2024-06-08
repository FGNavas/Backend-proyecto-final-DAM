package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario por su email.
     *
     * @param email el email del usuario.
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no se encuentra.
     */
    Optional<Usuario> findByEmail(String email);
}
