package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUsuario_IdAndVideoJuego_Id(Long usuarioId, Long videoJuegoId);

    List<UserGame> findByUsuarioIdAndStatus(Long usuarioId,GameStatus status);


}
