package com.example.mastercheffood.DataModel;

public class veg_data_model {

    String itemname,itemdes,itemprice,imgurl,details;


    veg_data_model()
    {

    }

    public veg_data_model(String itemname, String itemdes, String itemprice, String imgurl, String details) {
        this.itemname = itemname;
        this.itemdes = itemdes;
        this.itemprice = itemprice;
        this.imgurl = imgurl;
        this.details = details;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdes() {
        return itemdes;
    }

    public void setItemdes(String itemdes) {
        this.itemdes = itemdes;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}

