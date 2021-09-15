package com.example.cs571hw9;

public class SearchCard {
    private String media_name;
    private String backdrop_path;
    private String media_type;
    private String media_id;
    private String media_year;
    private String media_score;

    public SearchCard(String media_name, String backdrop_path, String media_type, String media_id, String media_year, String media_score) {
        this.media_name = media_name;
        this.backdrop_path = backdrop_path;
        this.media_type = media_type;
        this.media_id = media_id;
        this.media_year = media_year;
        this.media_score = media_score;
    }

    public String getMedia_year() {
        return media_year;
    }

    public String getMedia_score() {
        return media_score;
    }

    public SearchCard(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}

