package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad UserGame.
 */
public interface UserGameRepository extends JpaRepository<UserGame, Long> {

    /**
     * Encuentra un UserGame por el ID del usuario y el ID del video juego.
     *
     * @param usuarioId    el ID del usuario.
     * @param videoJuegoId el ID del video juego.
     * @return un Optional que contiene el UserGame si se encuentra, o vacío si no se encuentra.
     */
    Optional<UserGame> findByUsuario_IdAndVideoJuego_Id(Long usuarioId, Long videoJuegoId);

    /**
     * Encuentra todos los UserGame de un usuario por su estado.
     *
     * @param usuarioId el ID del usuario.
     * @param status    el estado del juego.
     * @return una lista de UserGame que corresponden al usuario y estado especificados.
     */
    List<UserGame> findByUsuarioIdAndStatus(Long usuarioId, GameStatus status);


}
