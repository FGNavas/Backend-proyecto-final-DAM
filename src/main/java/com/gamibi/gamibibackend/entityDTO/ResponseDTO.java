package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDTO {
    private String type;
    private Object data;

    public ResponseDTO(String type, Object data) {
        this.type = type;
        this.data = data;
    }
}
