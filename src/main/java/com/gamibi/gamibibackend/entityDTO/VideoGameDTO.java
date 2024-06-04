package com.gamibi.gamibibackend.entityDTO;

import com.gamibi.gamibibackend.entity.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class VideoGameDTO {
    public VideoGameDTO() {
    }

    public VideoGameDTO(VideoGameRAWGDTO rawgVideoGame, Date purchaseDate, boolean favorite, GameStatus status) {
        this.rawgVideoGame = rawgVideoGame;
        this.purchaseDate = purchaseDate;
        this.favorite = favorite;
        this.status = status;
    }

    private VideoGameRAWGDTO rawgVideoGame;

private Date purchaseDate;

private  boolean favorite;

private GameStatus status;

}
