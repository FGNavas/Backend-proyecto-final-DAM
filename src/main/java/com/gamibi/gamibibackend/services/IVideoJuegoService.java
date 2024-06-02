package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVideoJuegoService {
    List<VideoJuego> findAll();
    Page<VideoJuego> findAll(Pageable pageable);

    VideoJuego findById(Long id);

    VideoJuego save(VideoJuego videoGame);

    List<VideoJuego> findByName(String titulo);

    void delete(Long id);




}
