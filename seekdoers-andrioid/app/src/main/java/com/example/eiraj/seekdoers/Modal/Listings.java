package com.example.eiraj.seekdoers.Modal;

/**
 * Created by Abhishek on 02-08-2017.
 */

public class Listings {

    int ID;
    double Latitude;
    double Longitude;
    String AvatarImage;
    String Phone;
    String Address;
    String City;
    String StateL;
    String CountryLo;
    String JobTitle;
    String Rating;
    String MainImage;

    public String getMainImage() {
        return MainImage;
    }

    public void setMainImage(String mainImage) {
        MainImage = mainImage;
    }

    public String getRating() {

        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAvatarImage() {
        return AvatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        AvatarImage = avatarImage;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStateL() {
        return StateL;
    }

    public void setStateL(String stateL) {
        StateL = stateL;
    }

    public String getCountryLo() {
        return CountryLo;
    }

    public void setCountryLo(String countryLo) {
        CountryLo = countryLo;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
