package com.gamibi.gamibibackend.services;

import com.gamibi.gamibibackend.dao.IVideoGameDao;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import com.gamibi.gamibibackend.entityDTO.VideoGameRAWGDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoJuegoServiceImpl implements IVideoJuegoService {

    @Autowired
    private IVideoGameDao videoGameDao;
    @Autowired
    private RawgAPIService rawgAPIService;
    @Override
    @Transactional(readOnly=true)
    public List<VideoJuego> findAll() {
        return videoGameDao.findAll();
    }

    @Override
    @Transactional(readOnly=true)
    public Page<VideoJuego> findAll(Pageable pageable) {
        return videoGameDao.findAll(pageable);
    }

    @Override
    public VideoJuego findById(Long id) {
        return videoGameDao.getById(id);
    }

    @Override
    public VideoJuego save(VideoJuego videoGame) {
        return videoGameDao.save(videoGame);
    }

    @Override
    public List<VideoJuego> findByName(String titulo) {
        return videoGameDao.findbyName(titulo);
    }

    @Override
    public void delete(Long id) {
        videoGameDao.deleteById(id);

    }

    @Override
    public List<VideoGameDTO> findByNameAndUserId(String titulo, Long usuarioId) {
        List<VideoJuego> videoJuegos = videoGameDao.findByNameAndUserId(titulo, usuarioId);
        List<VideoGameDTO> videojuegosUsuario = new ArrayList<>();
        videoJuegos.forEach(v ->{
            Long id = v.getId();
            String videoGameRAWGDTO = rawgAPIService.getGameInfoById(String.valueOf(id));
// TODO
        });

        return videojuegosUsuario;
    }
    private VideoGameDTO convertToDTO(VideoJuego videoJuego) {
        VideoGameDTO dto = new VideoGameDTO();
        // Aquí setea los valores del DTO a partir de los datos de VideoJuego
        return dto;
    }

}
