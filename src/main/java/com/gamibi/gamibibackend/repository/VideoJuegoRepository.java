package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.VideoJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoJuegoRepository extends JpaRepository<VideoJuego, Long> {

    @Query("SELECT v FROM VideoJuego v WHERE LOWER(v.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<VideoJuego> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}
