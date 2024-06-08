package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para los resultados de búsqueda de videojuegos.
 */
@Getter
@Setter
public class VideoGameSearchResultDTO {
    private Long id;
    private String title;
    private String image;

    /**
     * Constructor para crear una instancia de VideoGameSearchResultDTO.
     *
     * @param id    El ID del videojuego.
     * @param title El título del videojuego.
     * @param image La imagen del videojuego.
     */
    public VideoGameSearchResultDTO(Long id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }
}
