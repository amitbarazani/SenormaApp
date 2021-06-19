package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class TemporaryVariables {
    public static ArrayList<LocationAttraction> chosenSightSeeingAttractions;
    public static ArrayList<LocationAttraction> chosenNightLifeAttractions;
    //public static ArrayList<Restaurant> restaurants;
    public static Boolean isSightSeeingChosen = false;
    public static Boolean isNightLifeChosen = false;
    public static Boolean isRestaurantsChosen = false;
    public static String startPointName;
    public static Double startPointLng;
    public static Double startPointLat;

    public static String tostring()
    {
        return "chosenSightSeeingAttractions:"+chosenSightSeeingAttractions + "\n"
                +"chosenNightLifeAttractions:"+chosenNightLifeAttractions +"\n"
                +"isSightSeeingChosen:"+isSightSeeingChosen +"\n"
                +"isNightLifeChosen:"+isNightLifeChosen +"\n"
                +"isRestaurantsChosen:"+isRestaurantsChosen +"\n"
                +"startPointName:"+startPointName +"\n"
                +"startPointLng:"+startPointLng +"\n"
                +"startPointLat:"+startPointLat +"\n";
    }
}
