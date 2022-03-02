package com.example.mastercheffood.DataModel;

public class ShowUserProfileModel {

    String personName,personEmail,personMobile,personAddress,documentID;


    public ShowUserProfileModel() {
    }

    public ShowUserProfileModel(String personName, String personEmail, String personMobile, String personAddress, String documentID) {
        this.personName = personName;
        this.personEmail = personEmail;
        this.personMobile = personMobile;
        this.personAddress = personAddress;
        this.documentID = documentID;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonMobile() {
        return personMobile;
    }

    public void setPersonMobile(String personMobile) {
        this.personMobile = personMobile;
    }

    public String getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(String personAddress) {
        this.personAddress = personAddress;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
