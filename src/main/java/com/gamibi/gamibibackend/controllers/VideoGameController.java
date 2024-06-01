package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.entity.VideoGame;
import com.gamibi.gamibibackend.services.IVideoGameService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class VideoGameController {
    private Logger log = LoggerFactory.getLogger(VideoGameController.class);
    @Autowired
    private IVideoGameService videoGameService;
    @Autowired
    @GetMapping("/games")
    public List<VideoGame> index(){return videoGameService.findAll();}

    @GetMapping("/games/page/{page}")
    public Page<VideoGame> index(@PathVariable Integer page){
        Pageable pageable = PageRequest.of(page,2);
        return  videoGameService.findAll(pageable);
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<?>show(@PathVariable Long id){
        VideoGame videoGame = null;
        Map<String, Object> response = new HashMap<>();
        // buscar videojuego
        try{
            videoGame = videoGameService.findById(id);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // cliente no encontrado

        if(videoGame == null){
            response.put("mensaje","No se ha encontrado el videojuego");
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }
        // cliente encontrado
        return new ResponseEntity<VideoGame>(videoGame,HttpStatus.OK);

    }
    @PostMapping("/addgame")
    public void create(@Valid @RequestBody VideoGame videoGame, BindingResult result){
        videoGameService.save(videoGame);
    }
    @GetMapping("/search/{titulo}")
    public ResponseEntity<?> findByTitulo(@PathVariable String titulo){
        List<VideoGame> lista = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        try{
            lista =  videoGameService.findByName(titulo);
        }catch (DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(lista.isEmpty()){
            response.put("mensaje","No se ha encontrado ningun videojuego que contenga: ".concat(titulo));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<VideoGame>>(lista,HttpStatus.OK);
    }
    @DeleteMapping("/games/{id}")
    public void delete(@PathVariable Long id){
        VideoGame videoGameBorrar=null;
        try{
            videoGameBorrar = videoGameService.findById(id);
            if(videoGameBorrar != null){
                videoGameService.delete(id);
            }
        } catch (DataAccessException e){
            log.error(e.getMostSpecificCause().getMessage());
        }
    }
}
