package com.gamibi.gamibibackend.dao;

import com.gamibi.gamibibackend.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para acceder a la base de datos de juegos de usuario.
 */
@Repository
public interface IUserGameDao extends JpaRepository<UserGame, Long> {
    /**
     * Busca un juego de usuario por su ID de usuario y ID de videojuego.
     *
     * @param usuarioId    El ID del usuario.
     * @param videoJuegoId El ID del videojuego.
     * @return El juego de usuario correspondiente, si existe.
     */
    Optional<UserGame> findByUsuario_IdAndVideoJuego_Id(Long usuarioId, Long videoJuegoId);

    /**
     * Busca los juegos favoritos de un usuario por su ID de usuario.
     *
     * @param usuarioId El ID del usuario.
     * @return Una lista de juegos de usuario favoritos del usuario correspondiente.
     */
    List<UserGame> findByUsuario_IdAndFavoriteTrue(Long usuarioId);
}