package com.gamibi.gamibibackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name= "games")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VideoGame implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "titulo")
    private String name;
    @Column(name = "descripcion")
    @Size(max= 255)
    private String description;

    @Column
    private Date released;

    //URL de la imagen
    @Column
    private String backgroundImage;

    @Column
    private Double rating;

    @Column
    // tiempo de juego en horas
    private int playtime;

    @ManyToMany
    @JoinTable(name = "videojuego_plataforma",
            joinColumns = @JoinColumn(name = "videojuego_id"),
            inverseJoinColumns = @JoinColumn(name = "plataforma_id"))
    private List<Plataformas> plataformasList = new ArrayList<>();





}

