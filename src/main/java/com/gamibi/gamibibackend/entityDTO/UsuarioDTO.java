package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO para representar un usuario con información básica y su lista de videojuegos.
 */
@Getter
@Setter
public class UsuarioDTO {
    private Long id;

    private String nombre;

    private String email;

    private String pais;

    private List<VideoGameDTO> listaJuegos;

    /**
     * Constructor por defecto.
     */
    public UsuarioDTO() {
    }

    /**
     * Constructor para inicializar el DTO con datos específicos.
     *
     * @param id          ID del usuario.
     * @param nombre      Nombre del usuario.
     * @param email       Email del usuario.
     * @param pais        País del usuario.
     * @param listaJuegos Lista de videojuegos asociados al usuario.
     */
    public UsuarioDTO(Long id, String nombre, String email, String pais, List<VideoGameDTO> listaJuegos) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.pais = pais;
        this.listaJuegos = listaJuegos;
    }
}
