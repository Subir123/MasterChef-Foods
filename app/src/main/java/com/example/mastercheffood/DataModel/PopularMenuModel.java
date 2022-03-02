package com.example.mastercheffood.DataModel;

public class PopularMenuModel
{
    String name,price,purl,details;
    PopularMenuModel()
    {

    }

    public PopularMenuModel(String name, String price, String purl, String details) {
        this.name = name;
        this.price = price;
        this.purl = purl;
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

    public String getPurl() {
        return purl;
    }

    public void setPurl(String purl) {
        this.purl = purl;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

