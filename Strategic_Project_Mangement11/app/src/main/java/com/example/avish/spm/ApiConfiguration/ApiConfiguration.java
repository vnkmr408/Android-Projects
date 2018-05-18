package com.example.avish.spm.ApiConfiguration;

/**
 * Created by Avish on 25-04-2017.
 */


public class ApiConfiguration {

    private String api = " http://resourcemanage.sfsd.sebizfinishingschool.com/API/";
    private String imageURL = "http://192.168.57.210/UserImages/";

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}