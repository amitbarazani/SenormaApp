package com.acebrico.royalcarribeanapp;

import android.graphics.Bitmap;

public class LocationAttraction {

    public String name;
    public Double lng;
    public Double lat;
    public Double distanceFromCurrentPlace;
    public Double rating = 0.0;
    public String description = "";
    public Boolean isOpen = false;
    public Boolean isChosen = false;
    public Bitmap pictureBitmap = null;

    public LocationAttraction(String name,Double lat,Double lng, Double rating, String description, Boolean isOpen, Boolean isChosen) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.isOpen = isOpen;
        this.isChosen = isChosen;
        this.lng = lng;
        this.lat = lat;
    }

    public LocationAttraction() {

    }

    @Override
    public String toString() {
        return "LocationAttraction{" +
                "name='" + name + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", isOpen=" + isOpen +
                ", isChosen=" + isChosen
                + '}';
    }
}

