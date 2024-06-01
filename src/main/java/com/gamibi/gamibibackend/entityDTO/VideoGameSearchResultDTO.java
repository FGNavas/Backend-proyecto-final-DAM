package com.gamibi.gamibibackend.entityDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoGameSearchResultDTO {
    private Long id;
    private String title;
    private String image;

    public VideoGameSearchResultDTO(Long id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }
}
