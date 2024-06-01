package com.gamibi.gamibibackend.controllers;

import com.gamibi.gamibibackend.entityDTO.VideoGameRAWG;
import com.gamibi.gamibibackend.services.RawgAPIService;
import com.google.gson.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/gamibiApi")
public class GameControllerRAWG {

    private final RawgAPIService rawgAPIService;
    private JsonObject jsonObject;

    public GameControllerRAWG(RawgAPIService rawgAPIService) {
        this.rawgAPIService = rawgAPIService;
    }

    @GetMapping("/gameSearch/{gameId}")
    public ResponseEntity<VideoGameRAWG> getGameInfo(@PathVariable String gameId) {
        String gameInfoJson = rawgAPIService.getGameInfoById(gameId);

        VideoGameRAWG gameInfo = convertirJsonAGameInfo(gameInfoJson);
        return ResponseEntity.ok().body(gameInfo);
    }

    private VideoGameRAWG convertirJsonAGameInfo(String gameInfoJson) {
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(gameInfoJson);
        jsonObject = jsonElement.getAsJsonObject();

        VideoGameRAWG videoGameRAWG = new VideoGameRAWG();

        if (jsonObject.has("id")) {
            JsonElement descripcionElement = jsonObject.get("id");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setId(jsonObject.get("id").getAsLong());
            } else {
                videoGameRAWG.setId(1L);
            }
        }

        if (jsonObject.has("name")) {
            JsonElement descripcionElement = jsonObject.get("name");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setName(jsonObject.get("name").getAsString());
            } else {
                videoGameRAWG.setName("No name");
            }
        }

        if (jsonObject.has("description_raw")) {
            JsonElement descripcionElement = jsonObject.get("description_raw");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setDescripcion_raw(jsonObject.get("description_raw").getAsString());
            } else {
                videoGameRAWG.setDescripcion_raw("{ No description }");
            }
        }
        if (jsonObject.has("metacritic")) {
            JsonElement descripcionElement = jsonObject.get("metacritic");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setMetacritic(jsonObject.get("metacritic").getAsDouble());
            } else {
                videoGameRAWG.setMetacritic(0.0);
            }
        }
        if (jsonObject.has("rating")) {
            JsonElement descripcionElement = jsonObject.get("rating");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setRating(jsonObject.get("rating").getAsDouble());
            } else {
                videoGameRAWG.setRating(0.0);
            }
        }
        if (jsonObject.has("rating_top")) {
            JsonElement descripcionElement = jsonObject.get("rating_top");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setRating_top(jsonObject.get("rating_top").getAsDouble());
            } else {
                videoGameRAWG.setRating_top(0.0);
            }
        }
        if (jsonObject.has("released")) {
            JsonElement descripcionElement = jsonObject.get("released");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setReleased(jsonObject.get("released").getAsString());
            } else {
                videoGameRAWG.setReleased("");
            }
        }
        if (jsonObject.has("playtime")) {
            JsonElement descripcionElement = jsonObject.get("playtime");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setPlaytime(jsonObject.get("playtime").getAsDouble());
            } else {
                videoGameRAWG.setPlaytime(0.0);
            }
        }
        if (jsonObject.has("background_image")) {
            JsonElement descripcionElement = jsonObject.get("background_image");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setBackground_image(jsonObject.get("background_image").getAsString());
            } else {
                videoGameRAWG.setBackground_image("");
            }
        }
        if (jsonObject.has("background_image_additional")) {
            JsonElement descripcionElement = jsonObject.get("background_image_additional");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setBackground_image_additional(jsonObject.get("background_image_additional").getAsString());
            } else {
                videoGameRAWG.setBackground_image_additional("");
            }
        }
        if (jsonObject.has("saturated_color")) {
            JsonElement descripcionElement = jsonObject.get("saturated_color");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setSaturated_color(jsonObject.get("saturated_color").getAsString());
            } else {
                videoGameRAWG.setSaturated_color("");
            }
        }
        if (jsonObject.has("dominant_color")) {
            JsonElement descripcionElement = jsonObject.get("dominant_color");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setDominant_color(jsonObject.get("dominant_color").getAsString());
            } else {
                videoGameRAWG.setDominant_color("");
            }
        }
        if (jsonObject.has("website")) {
            JsonElement descripcionElement = jsonObject.get("website");
            if (!descripcionElement.isJsonNull()) {
                videoGameRAWG.setWebsite(jsonObject.get("website").getAsString());
            } else {
                videoGameRAWG.setWebsite("");
            }
        }
        if (jsonObject.has("genres")) {
            JsonArray genresArray = jsonObject.getAsJsonArray("genres");
            List<String> genreNames = new ArrayList<>();
            genresArray.forEach(genreElement -> {
                if (!genreElement.isJsonNull()) {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    JsonElement nameElement = genreObject.get("name");
                    if (!nameElement.isJsonNull()) {
                        String genreName = nameElement.getAsString();
                        genreNames.add(genreName);
                    } else {
                        genreNames.add("Sin genero");
                    }
                }
            });
            videoGameRAWG.setGenres(genreNames);
        }
        if (jsonObject.has("publishers")) {
            JsonArray publishersArray = jsonObject.getAsJsonArray("publishers");
            List<String> publisherNames = new ArrayList<>();
            publishersArray.forEach(genreElement -> {
                if (!genreElement.isJsonNull()) {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    JsonElement nameElement = genreObject.get("name");
                    if (!nameElement.isJsonNull()) {
                        String genreName = nameElement.getAsString();
                        publisherNames.add(genreName);
                    } else {
                        publisherNames.add("Sin publisher");
                    }
                }
            });
            videoGameRAWG.setPublishers(publisherNames);
        }
        if (jsonObject.has("platforms")) {
            JsonArray platformsArray = jsonObject.getAsJsonArray("platforms");
            List<String> platformNames = new ArrayList<>();
            for (JsonElement platformElement : platformsArray) {
                JsonObject platformObject = platformElement.getAsJsonObject().getAsJsonObject("platform");
                if (platformObject.has("name")) {
                    String nameElement = platformObject.get("name").getAsString();
                    platformNames.add(nameElement);
                } else {
                    platformNames.add("Unknown Platform");
                }
            }
            videoGameRAWG.setPlatforms(platformNames);
        }


        return videoGameRAWG;
    }

}

