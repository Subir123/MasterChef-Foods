package com.example.mastercheffood.DataModel;

public class OrderHistoryModel {

    String personName,orderAmount,currentDate,itemsBooked,documentID;


    public OrderHistoryModel() {
    }

    public OrderHistoryModel(String personName, String orderAmount, String currentDate, String itemsBooked, String personEmail, String personMobile, String currentTime, String personAddress, String documentID) {
        this.personName = personName;
        this.orderAmount = orderAmount;
        this.currentDate = currentDate;
        this.itemsBooked = itemsBooked;
        this.documentID = documentID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getItemsBooked() {
        return itemsBooked;
    }

    public void setItemsBooked(String itemsBooked) {
        this.itemsBooked = itemsBooked;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

}
