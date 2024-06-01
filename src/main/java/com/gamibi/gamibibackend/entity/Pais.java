package com.gamibi.gamibibackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "countries")
public class Pais implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name= "paisId")
    private String paisId;
    @Column(name = "countriename", nullable = false, length = 100)
    private String name;


}
