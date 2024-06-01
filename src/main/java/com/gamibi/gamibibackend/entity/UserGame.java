package com.gamibi.gamibibackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@Entity
@Table(name = "user_games")
public class UserGame implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "videojuegos_id", nullable = false)
    private VideoJuego videoJuego;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "favorite")
    private boolean favorite;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GameStatus status;

    @Column(name = "rating")
    private int rating;

    @Column(name = "completion_date")
    private Date completionDate;



    public UserGame() {

    }


    public UserGame(Usuario usuario, VideoJuego videoJuego, Date purchaseDate, boolean favorite, GameStatus status) {
        this.usuario = usuario;
        this.videoJuego = videoJuego;
        this.purchaseDate = purchaseDate;
        this.favorite = favorite;
        this.status = status;
    }

}