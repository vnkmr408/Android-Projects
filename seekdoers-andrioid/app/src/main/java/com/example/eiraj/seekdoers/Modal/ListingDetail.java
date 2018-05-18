package com.example.eiraj.seekdoers.Modal;

import java.util.List;

/**
 * Created by Abhishek on 11-08-2017.
 */

public class ListingDetail {

    Double Latitude;
    Double Longitude;
    String Description;
    String PhoneNo;
    String Address;
    String Email;
    String Website;
    String GallImages;
    List<String> imageList;
    String AvgRating;

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getGallImages() {
        return GallImages;
    }

    public void setGallImages(String gallImages) {
        GallImages = gallImages;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getAvgRating() {
        return AvgRating;
    }

    public void setAvgRating(String avgRating) {
        AvgRating = avgRating;
    }
}
