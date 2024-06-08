package com.gamibi.gamibibackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
/**
 * Entidad que representa un videojuego en la base de datos.
 */
@Entity
@Table(name = "videojuegos")
public class VideoJuego implements Serializable {

    @Id
    private Long id;
    @Column(name = "titulo")
    private String titulo;
    public VideoJuego() {
    }
    public VideoJuego(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }
}
