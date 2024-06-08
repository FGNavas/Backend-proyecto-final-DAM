package com.gamibi.gamibibackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Representa el estado de una solicitud a una API.
 */
@Entity
public class ApiRequestState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID del último juego procesado en la solicitud.
     */
    private Long lastGameId;
}
