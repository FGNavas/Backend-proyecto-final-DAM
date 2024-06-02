package com.gamibi.gamibibackend.dao;

import com.gamibi.gamibibackend.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserGameDao extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUsuario_IdAndVideoJuego_Id(Long usuarioId, Long videoJuegoId);
}