package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para encapsular las respuestas de la API, incluyendo el tipo de respuesta y los datos asociados.
 */
@Getter
@Setter
public class ResponseDTO {
    private String type;
    private Object data;

    /**
     * Constructor para inicializar el DTO con tipo y datos específicos.
     *
     * @param type Tipo de la respuesta.
     * @param data Datos de la respuesta.
     */
    public ResponseDTO(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}
