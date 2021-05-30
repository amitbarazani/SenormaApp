package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;

import java.util.ArrayList;

public class ShowSightSeeingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean;
    ListView lv_locations;
    //
    PointOfInterest[] pointsOfInterest;
    ArrayList<LocationAttraction> locationAttractions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sight_seeing);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        lv_locations = findViewById(R.id.lv_locations);

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
            if(point != null) {
                Log.d("TAG", "point:" + point.toString());
                LocationAttraction templocation = new LocationAttraction();
                templocation.name = point.getName();
                templocation.lat = point.getGeoCode().getLatitude();
                templocation.lng = point.getGeoCode().getLongitude();

                locationAttractions.add(templocation);
            }
        }
        LocationAttractionAdapter locationAttractionAdapter = new LocationAttractionAdapter(locationAttractions,ShowSightSeeingActivity.this);
        lv_locations.setAdapter(locationAttractionAdapter);
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

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