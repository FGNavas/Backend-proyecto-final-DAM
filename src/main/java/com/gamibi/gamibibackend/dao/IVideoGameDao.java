package com.gamibi.gamibibackend.dao;


import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameRAWGDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IVideoGameDao extends JpaRepository<VideoJuego, Long> {
    @Query("SELECT v FROM VideoJuego v WHERE v.titulo LIKE %:titulo%")
    List<VideoJuego> findByName(@Param("titulo") String titulo);


    @Query("SELECT v FROM VideoJuego v WHERE v.titulo LIKE %:titulo% AND v.usuario.id = :usuarioId")
    List<VideoJuego> findByNameAndUserId(@Param("titulo") String titulo, @Param("usuarioId") Long usuarioId);

    @Query("SELECT u FROM UserGame u WHERE u.usuario.id = :userId AND u.videoJuego.id = :videoJuegoId")
    Optional<UserGame> findByUserIdAndVideoJuegoId(@Param("userId") Long userId, @Param("videoJuegoId") Long videoJuegoId);
    @Query("SELECT u.videoJuego FROM UserGame u WHERE u.usuario.id = :userId AND u.favorite = true")
    List<VideoJuego> findFavoriteGamesByUserId(@Param("userId") Long userId);
    @Query("SELECT u.videoJuego FROM UserGame u WHERE u.usuario.id = :userId")
    List<VideoJuego> findAllGamesByUserId(@Param("userId") Long userId);



}




