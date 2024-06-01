package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.dao.IVideoGameDao;
import com.gamibi.gamibibackend.entity.VideoGame;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoGameServiceImpl implements IVideoGameService {

    @Autowired
    private IVideoGameDao videoGameDao;

    @Override
    @Transactional(readOnly=true)
    public List<VideoGame> findAll() {
        return (List<VideoGame>) videoGameDao.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Page<VideoGame> findAll(Pageable pageable) {
        return videoGameDao.findAll(pageable);
    }

    @Override
    public VideoGame findById(Long id) {
        return videoGameDao.getById(id);
    }

    @Override
    public VideoGame save(VideoGame videoGame) {
        return videoGameDao.save(videoGame);
    }

    @Override
    public List<VideoGame> findByName(String titulo) {
        return videoGameDao.findbyName(titulo);
    }

    @Override
    public void delete(Long id) {
        videoGameDao.deleteById(id);

    }
}
