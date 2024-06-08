package com.gamibi.gamibibackend.dao;

import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.VideoJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para acceder a la base de datos de VideoJuegos.
 */
public interface IVideoGameDao extends JpaRepository<VideoJuego, Long> {

    /**
     * Busca videojuegos por nombre.
     *
     * @param titulo El título del videojuego a buscar.
     * @return Una lista de videojuegos que coinciden con el título especificado.
     */
    @Query("SELECT v FROM VideoJuego v WHERE v.titulo LIKE %:titulo%")
    List<VideoJuego> findByName(@Param("titulo") String titulo);

    /**
     * Busca videojuegos por nombre y ID de usuario.
     *
     * @param titulo    El título del videojuego a buscar.
     * @param usuarioId El ID del usuario.
     * @return Una lista de videojuegos que coinciden con el título especificado y pertenecen al usuario especificado.
     */
    @Query("SELECT v FROM VideoJuego v JOIN UserGame ug ON v.id = ug.videoJuego.id WHERE v.titulo LIKE %:titulo% AND ug.usuario.id = :usuarioId")
    List<VideoJuego> findByNameAndUserId(@Param("titulo") String titulo, @Param("usuarioId") Long usuarioId);

    /**
     * Busca el juego de un usuario por su ID de usuario y ID de videojuego.
     *
     * @param userId       El ID del usuario.
     * @param videoJuegoId El ID del videojuego.
     * @return El juego del usuario correspondiente, si existe.
     */
    @Query("SELECT u FROM UserGame u WHERE u.usuario.id = :userId AND u.videoJuego.id = :videoJuegoId")
    Optional<UserGame> findByUserIdAndVideoJuegoId(@Param("userId") Long userId, @Param("videoJuegoId") Long videoJuegoId);

    /**
     * Busca los juegos favoritos de un usuario por su ID de usuario.
     *
     * @param userId El ID del usuario.
     * @return Una lista de videojuegos favoritos del usuario correspondiente.
     */
    @Query("SELECT u.videoJuego FROM UserGame u WHERE u.usuario.id = :userId AND u.favorite = true")
    List<VideoJuego> findFavoriteGamesByUserId(@Param("userId") Long userId);

    /**
     * Busca todos los juegos de un usuario por su ID de usuario.
     *
     * @param userId El ID del usuario.
     * @return Una lista de todos los videojuegos del usuario correspondiente.
     */
    @Query("SELECT u.videoJuego FROM UserGame u WHERE u.usuario.id = :userId")
    List<UserGame> findAllGamesByUserId(@Param("userId") Long userId);
}




