package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    Optional<UserGame> findByUserIdAndGameId(Long userId, Long gameId);
}
