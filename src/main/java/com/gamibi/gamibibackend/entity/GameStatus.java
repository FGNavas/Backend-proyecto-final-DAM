package com.gamibi.gamibibackend.entity;

/**
 * Enumera los posibles estados de un juego.
 */
public enum GameStatus {
    /**
     * El juego está siendo jugado actualmente.
     */
    JUGANDO,
    /**
     * El juego está pendiente para ser jugado.
     */
    PENDIENTE,
    /**
     * El juego ha sido finalizado.
     */
    FINALIZADO,
    /**
     * El juego ha sido abandonado.
     */
    ABANDONADO
}
