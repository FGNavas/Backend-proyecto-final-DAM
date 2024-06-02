package com.gamibi.gamibibackend.entityDTO;

import com.gamibi.gamibibackend.entity.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class VideoGameDTO {


private VideoGameRAWGDTO rawgVideoGame;

private Date purchaseDate;

private  boolean favorite;

private GameStatus status;

}
