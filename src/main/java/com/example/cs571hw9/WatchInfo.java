package com.example.cs571hw9;

public class WatchInfo {

    private String key;
    private String poster_path;
    private String name;

    public WatchInfo(String key, String poster_path, String name) {
        this.key = key;
        this.poster_path = poster_path;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getName() {
        return name;
    }
}
