package com.example.mastercheffood.DataModel;

public class SliderDataModel {

    // string for our image url.
    private String imgUrl,title;

    // empty constructor which is
    // required when using Firebase.
    public SliderDataModel() {
    }

    public SliderDataModel(String imgUrl, String title) {
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
