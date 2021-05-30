package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ShowSightSeeingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean;
    RecyclerView rv_locations;
    //
    PointOfInterest[] pointsOfInterest;
    ArrayList<LocationAttraction> locationAttractions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sight_seeing);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        rv_locations = findViewById(R.id.rv_locations);

        Double lat = getIntent().getDoubleExtra("lat",0.0);
        Double lng = getIntent().getDoubleExtra("lng",0.0);
        loadLocations(lat,lng);


        //
        img_royalcarribean.setOnClickListener(this);
    }


    public void loadLocations(Double lat,Double lng)
    {
        locationAttractions = new ArrayList<>();
        Amadeus amadeus = Amadeus
                .builder("xzDnM1eqDw4TlRIjAdqQ4LOxbZ015ida", "OBXSABf3MkqJD2ob")
                .build();
        try {
            pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                    .with("latitude", lat)
                    .and("longitude", lng)
                    .and("category","SIGHTS"));
        } catch (ResponseException e) {
            e.printStackTrace();
            return;
        }
        for (PointOfInterest point: pointsOfInterest) {
            Log.d("TAG", "point:"+point.toString());
            LocationAttraction templocation = new LocationAttraction();
            templocation.name = point.getName();
            templocation.lat = point.getGeoCode().getLatitude();
            templocation.lng = point.getGeoCode().getLongitude();
            locationAttractions.add(templocation);
        }


    }



    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ShowSightSeeingActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();
        }
    }
}