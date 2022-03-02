package com.example.mastercheffood.DataModel;

public class search_data_model {

    public String name, price, des, image, details;

    public search_data_model() {

    }

    public search_data_model(String name, String price, String des, String image, String details) {
        this.name = name;
        this.price = price;
        this.des = des;
        this.image = image;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

