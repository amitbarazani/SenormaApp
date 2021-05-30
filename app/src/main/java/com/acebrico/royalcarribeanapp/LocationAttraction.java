package com.acebrico.royalcarribeanapp;

import android.graphics.Bitmap;

public class LocationAttraction {

    public String name;
    public Double lng;
    public Double lat;
    public Double rating;
    public String description;
    public Boolean isOpen;
    public Boolean isChosen = false;
    public Bitmap picture;

    public LocationAttraction(String name,Double lat,Double lng, Double rating, String description, Boolean isOpen, Boolean isChosen, Bitmap picture) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.isOpen = isOpen;
        this.isChosen = isChosen;
        this.picture = picture;
        this.lng = lng;
        this.lat = lat;
    }

    public LocationAttraction() {

    }
}
