package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;

public class TripSummaryActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img_royalcarribean;
    TextView tv_startPoint,tv_endPoint,tv_distanceToEndPoint;
    ListView lv_locations;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_save = findViewById(R.id.btn_save);
        tv_endPoint = findViewById(R.id.tv_endPoint);
        tv_distanceToEndPoint = findViewById(R.id.tv_distanceToEndPoint);
        tv_startPoint = findViewById(R.id.tv_startingPoint);
        lv_locations = findViewById(R.id.lv_locations);
        //
        tv_startPoint.setText(TemporaryVariables.startPointName);
        tv_endPoint.setText(TemporaryVariables.startPointName);
        sortAttractions();
        LocationSummaryAdapter locationSummaryAdapter = new LocationSummaryAdapter(
                TemporaryVariables.chosenAttractions,TripSummaryActivity.this);
        lv_locations.setAdapter(locationSummaryAdapter);
        //
        img_royalcarribean.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }
    public void sortAttractions()
    {
        Collections.sort(TemporaryVariables.chosenAttractions, new Comparator<LocationAttraction>() {
            @Override
            public int compare(LocationAttraction locationAttraction, LocationAttraction t1) {
                Double temp1 = locationAttraction.distanceFromCurrentPlace;
                Double temp2 = t1.distanceFromCurrentPlace;
                return temp1.compareTo(temp2);
            }
        });
        updateDistances();
        //Collections.reverse(TemporaryVariables.chosenAttractions);
    }


    public void updateDistances()
    {
        String distanceFromLastLocation = distance(
                TemporaryVariables.startPointLat,TemporaryVariables.startPointLng,
                TemporaryVariables.chosenAttractions.get(0).lat,TemporaryVariables.chosenAttractions.get(0).lng) + "";

        switch (TemporaryVariables.chosenAttractions.size())
        {
            case 2:
                distanceFromLastLocation = distance(
                        TemporaryVariables.startPointLat,TemporaryVariables.startPointLng,
                        TemporaryVariables.chosenAttractions.get(1).lat,TemporaryVariables.chosenAttractions.get(1).lng) + "";
                updateDistanceByPreviousLocation(1);
                break;
            case 3:
                distanceFromLastLocation = distance(
                        TemporaryVariables.startPointLat,TemporaryVariables.startPointLng,
                        TemporaryVariables.chosenAttractions.get(2).lat,TemporaryVariables.chosenAttractions.get(2).lng) + "";
                updateDistanceByPreviousLocation(1);
                updateDistanceByPreviousLocation(2);
                break;
            case 4:
                distanceFromLastLocation = distance(
                        TemporaryVariables.startPointLat,TemporaryVariables.startPointLng,
                        TemporaryVariables.chosenAttractions.get(3).lat,TemporaryVariables.chosenAttractions.get(3).lng) + "";
                updateDistanceByPreviousLocation(1);
                updateDistanceByPreviousLocation(2);
                updateDistanceByPreviousLocation(3);
                break;
        }
        tv_distanceToEndPoint.setText("Distance:"+distanceFromLastLocation.substring(0,4) +"KM");

    }

    public void updateDistanceByPreviousLocation(Integer index)
    {
        LocationAttraction current = TemporaryVariables.chosenAttractions.get(index);
        LocationAttraction previous = TemporaryVariables.chosenAttractions.get(index - 1);
        current.distanceFromCurrentPlace = distance(previous.lat,previous.lng,current.lat,current.lng);
        TemporaryVariables.chosenAttractions.set(index,current);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onClick(View view) {
        if (view == img_royalcarribean) {
            Intent intent = new Intent(TripSummaryActivity.this, MenuClientActivity.class);
            startActivity(intent);
            finish();
        }else if(view == btn_save)
        {
            //TODO:save screenshot of the page
        }
    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent(TripSummaryActivity.this,TripPlannerActivity.class));
        finish();
    }
}