package com.gamibi.gamibibackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Entidad que representa un usuario en la base de datos.
 */
@Entity
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @OneToMany(mappedBy = "usuario")
    private List<UserGame> juegos;

    /**
     * Obtiene el nombre del país del usuario.
     *
     * @return El nombre del país del usuario.
     */
    public String getNombrePais() {
        return pais.getName();
    }

}
