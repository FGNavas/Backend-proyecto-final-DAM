package com.gamibi.gamibibackend.entityDTO;

import com.gamibi.gamibibackend.entity.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DTO para representar un videojuego con información adicional de usuario.
 */
@Setter
@Getter
public class VideoGameDTO {
    /**
     * Constructor por defecto.
     */
    public VideoGameDTO() {
    }

    /**
     * Constructor para inicializar el DTO con datos específicos.
     *
     * @param rawgVideoGame Información del videojuego desde la API RAWG.
     * @param purchaseDate  Fecha de compra del videojuego.
     * @param favorite      Indica si el videojuego es favorito.
     * @param status        Estado del videojuego.
     */
    public VideoGameDTO(VideoGameRAWGDTO rawgVideoGame, Date purchaseDate, boolean favorite, GameStatus status) {
        this.rawgVideoGame = rawgVideoGame;
        this.purchaseDate = purchaseDate;
        this.favorite = favorite;
        this.status = status;
    }

    private VideoGameRAWGDTO rawgVideoGame;

    private Date purchaseDate;

    private boolean favorite;

    private GameStatus status;

}
