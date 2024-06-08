package com.gamibi.gamibibackend.repository;

import com.gamibi.gamibibackend.entity.VideoJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la entidad VideoJuego.
 */
public interface VideoJuegoRepository extends JpaRepository<VideoJuego, Long> {
    /**
     * Busca videojuegos cuyo título contenga una cadena específica, ignorando mayúsculas y minúsculas.
     *
     * @param titulo el título a buscar.
     * @return una lista de videojuegos que coinciden con el título dado.
     */
    @Query("SELECT v FROM VideoJuego v WHERE LOWER(v.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<VideoJuego> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}
