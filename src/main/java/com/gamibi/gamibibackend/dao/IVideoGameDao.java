package com.gamibi.gamibibackend.dao;


import com.gamibi.gamibibackend.entity.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IVideoGameDao extends JpaRepository<VideoGame, Long> {
    @Query("select v from VideoGame v where v.name like %?1%")
    List<VideoGame> findbyName(String titulo);

}
