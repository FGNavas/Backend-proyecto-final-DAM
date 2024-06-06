package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameRAWGDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IVideoJuegoService {
    List<VideoJuego> findAll();
    Page<VideoJuego> findAll(Pageable pageable);
    VideoJuego findById(Long id);
    VideoJuego save(VideoJuego videoGame);
    void delete(Long id);
    List<VideoJuego> findByName(String titulo);
    List<VideoJuego> findByNameAndUserId(String titulo, Long usuarioId);
    Optional<UserGame> findByUserIdAndVideoJuegoId(Long userId, Long videoJuegoId);
    VideoGameRAWGDTO getGameInfoFromApi(String id);
    VideoGameDTO getVideoGameDTO(String titulo, Long userId);
    List<VideoGameDTO> findFavoriteGamesByUserId(Long userId);

    List<VideoGameDTO> findAllGamesByUserId(Long userId);

    void addGameToUser(Long userId, Long gameId, Date purchaseDate, boolean favorite, GameStatus status, int rating);
    void updateFavoriteStatus(Long userId, Long gameId, boolean favorite);
    void removeGameFromUser(Long userId, Long gameId);
    void updateGameStatus(Long userId, Long gameId, GameStatus status);

    List<VideoGameDTO> findPendingGamesByUserId(Long userId);

}

