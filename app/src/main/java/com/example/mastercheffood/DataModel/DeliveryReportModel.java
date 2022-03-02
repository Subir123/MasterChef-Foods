package com.example.mastercheffood.DataModel;

public class DeliveryReportModel {

    String name,email,mobile,address,date,bookingTime,deliveryTime,bookingId,bookedItems, payableAmount,documentID;

    public DeliveryReportModel() {
    }

    public DeliveryReportModel(String name, String email, String mobile, String address, String date, String bookingTime, String deliveryTime, String bookingId, String bookedItems, String payableAmount, String documentID) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.date = date;
        this.bookingTime = bookingTime;
        this.deliveryTime = deliveryTime;
        this.bookingId = bookingId;
        this.bookedItems = bookedItems;
        this.payableAmount = payableAmount;
        this.documentID = documentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookedItems() {
        return bookedItems;
    }

    public void setBookedItems(String bookedItems) {
        this.bookedItems = bookedItems;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}

