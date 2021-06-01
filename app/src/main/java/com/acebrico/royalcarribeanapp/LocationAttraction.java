package com.acebrico.royalcarribeanapp;

public class LocationAttraction {

    public String name;
    public Double lng;
    public Double lat;
    public Double rating = 0.0;
    public String description = "";
    public Boolean isOpen = false;
    public Boolean isChosen = false;
    public String pictureUrl = "";

    public LocationAttraction(String name,Double lat,Double lng, Double rating, String description, Boolean isOpen, Boolean isChosen, String pictureUrl) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.isOpen = isOpen;
        this.isChosen = isChosen;
        this.pictureUrl = pictureUrl;
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
                ", isChosen=" + isChosen +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}

