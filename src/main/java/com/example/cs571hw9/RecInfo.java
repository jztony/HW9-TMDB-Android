package com.example.cs571hw9;

public class RecInfo {
    private String poster_path;
    private String type;
    private String id;

    public RecInfo(String poster_path, String type, String id) {
        this.poster_path = poster_path;
        this.type = type;
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
