package com.gamibi.gamibibackend.entityDTO;

import java.util.List;

public class VideoGameRAWG {
    private Long id;
    private String name;
    private String descripcion_raw;

    private Double metacritic;
    private Double rating;
    private Double rating_top;
    private List<String> platforms;
    private String released;
    private Double playtime;
    private String background_image;
    private String background_image_additional;

    private String saturated_color;
    private String dominant_color;

    private String website;

    private List<String> genres;

    private List<String> publishers;

    public VideoGameRAWG() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            this.name = "{No name}";
        }
        this.name = name.trim();
    }

    public String getDescripcion_raw() {
        return descripcion_raw;
    }

    public void setDescripcion_raw(String descripcion_raw) {
        if (descripcion_raw == null) {
            this.descripcion_raw = "{No description}";
            return;
        }

        String[] parts = descripcion_raw.split("Español");
        if (parts.length > 1) {
            this.descripcion_raw = parts[1];
        } else {
            this.descripcion_raw = descripcion_raw;
        }
    }

    public Double getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(Double metacritic) {
        this.metacritic = metacritic;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating_top() {
        return rating_top;
    }

    public void setRating_top(Double rating_top) {
        this.rating_top = rating_top;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public Double getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Double playtime) {
        this.playtime = playtime;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getBackground_image_additional() {
        return background_image_additional;
    }

    public void setBackground_image_additional(String background_image_additional) {
        this.background_image_additional = background_image_additional;
    }

    public String getSaturated_color() {
        return saturated_color;
    }

    public void setSaturated_color(String saturated_color) {
        this.saturated_color = saturated_color;
    }

    public String getDominant_color() {
        return dominant_color;
    }

    public void setDominant_color(String dominant_color) {
        this.dominant_color = dominant_color;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }
}
