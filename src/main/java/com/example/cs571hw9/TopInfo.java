package com.example.cs571hw9;

public class TopInfo {
    private String name;
    private String path;
    private String type;
    private String id;

    public TopInfo(String name, String path, String type, String id) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
