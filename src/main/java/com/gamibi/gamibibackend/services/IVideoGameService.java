package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.entity.VideoGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVideoGameService {
    List<VideoGame> findAll();
    Page<VideoGame> findAll(Pageable pageable);

    VideoGame findById(Long id);

    VideoGame save(VideoGame videoGame);

    List<VideoGame> findByName(String titulo);

    void delete(Long id);


}
