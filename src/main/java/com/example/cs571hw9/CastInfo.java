package com.example.cs571hw9;

public class CastInfo {
    private String cast_name;
    private String profile_path;

    public CastInfo(String cast_name, String profile_path) {
        this.cast_name = cast_name;
        this.profile_path = profile_path;
    }

    public String getCast_name() {
        return cast_name;
    }

    public void setCast_name(String cast_name) {
        this.cast_name = cast_name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
