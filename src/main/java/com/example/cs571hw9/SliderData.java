package com.example.cs571hw9;

public class SliderData {
    // image url is used to
    // store the url of image
    private String imgUrl;
    private String imgType;
    private String imgId;

    public SliderData(String imgUrl, String imgType, String imgId) {
        this.imgUrl = imgUrl;
        this.imgType = imgType;
        this.imgId = imgId;
    }

    public String getImgType() {
        return imgType;
    }

    public String getImgId() {
        return imgId;
    }

    // Getter method
    public String getImgUrl() {
        return imgUrl;
    }

    // Setter method
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
