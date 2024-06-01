package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UsuarioDTO {
    private Long id;

    private String nombre;

    private String email;

    private String pais;

    private List<VideoGameDTO> listaJuegos;


}
