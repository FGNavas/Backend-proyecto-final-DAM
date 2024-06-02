package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.dao.IVideoGameDao;
import com.gamibi.gamibibackend.entity.UserGame;
import com.gamibi.gamibibackend.entity.VideoJuego;
import com.gamibi.gamibibackend.entityDTO.VideoGameDTO;
import com.gamibi.gamibibackend.services.IVideoJuegoService;
import com.gamibi.gamibibackend.services.RawgAPIService;
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
public class VideoJuegoController {
    private Logger log = LoggerFactory.getLogger(VideoJuegoController.class);
    @Autowired
    private IVideoJuegoService videoGameService;
    @Autowired
    private IVideoGameDao videoGameDao;

    private RawgAPIService rawgAPIService;

    @Autowired
    @GetMapping("/games")
    public List<VideoJuego> index() {
        return videoGameService.findAll();
    }

    @GetMapping("/games/page/{page}")
    public Page<VideoJuego> index(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 2);
        return videoGameService.findAll(pageable);
    }


    @GetMapping("/game/{id}")
    public ResponseEntity<?> show(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        VideoJuego videoGame = null;
        videoGame = videoGameService.findById(id);

        if (videoGame == null) {
            return new ResponseEntity<>("No se ha encontrado el videojuego", HttpStatus.NOT_FOUND);
        }

        if (userId != null) {
            // Verificar si el usuario tiene el juego en su lista
            Optional<UserGame> userGame = null;
            userGame = videoGameDao.findByUserIdAndVideoJuegoId(userId,videoGame.getId());
            // Pseudocódigo
            if (userGame != null){
                VideoGameDTO videoGameDTO = null;
                // Llenar videoGameDTO con los datos del videojuego y del usuario
                String rawgResponse = rawgAPIService.getGameInfoById(String.valueOf(id));


                return new ResponseEntity<>(videoGameDTO, HttpStatus.OK);
            } else{
                // Obtener información del videojuego de la API de RAWG
                String gameInfoJson = rawgAPIService.getGameInfoById(String.valueOf(id));
                VideoGameRAWGDTO rawgVideoGame = convertirJsonAGameInfo(gameInfoJson);
                return new ResponseEntity<>(rawgVideoGame, HttpStatus.OK);
            }
        } else {
            // Si no se proporciona el userId, devolver solo los datos del videojuego
            return new ResponseEntity<>(videoGame, HttpStatus.OK);
        }
    }

    @PostMapping("/addgame")
    public void create(@Valid @RequestBody VideoJuego videoGame, BindingResult result) {
        videoGameService.save(videoGame);
    }

    @GetMapping("/search/{titulo}")
    public ResponseEntity<?> findByTitulo(@PathVariable String titulo) {
        List<VideoJuego> lista = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        try {
            lista = videoGameService.findByName(titulo);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (lista.isEmpty()) {
            response.put("mensaje", "No se ha encontrado ningun videojuego que contenga: ".concat(titulo));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<List<VideoJuego>>(lista, HttpStatus.OK);
    }

    @DeleteMapping("/games/{id}")
    public void delete(@PathVariable Long id) {
        VideoJuego videoGameBorrar = null;
        try {
            videoGameBorrar = videoGameService.findById(id);
            if (videoGameBorrar != null) {
                videoGameService.delete(id);
            }
        } catch (DataAccessException e) {
            log.error(e.getMostSpecificCause().getMessage());
        }
    }
}
