package com.gamibi.gamibibackend.entityDTO;

import com.gamibi.gamibibackend.entity.GameStatus;
import com.gamibi.gamibibackend.entity.Usuario;
import com.gamibi.gamibibackend.entity.VideoJuego;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class VideoGameDTO {


private VideoGameRAWG rawgVideoGame;

private Date purchaseDate;

private  boolean favorite;

private GameStatus status;

}
