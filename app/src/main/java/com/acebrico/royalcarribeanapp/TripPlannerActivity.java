package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
//
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.amadeus.resources.PointOfInterest;


import java.util.List;

public class TripPlannerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageView img_royalcarribean;
    Switch swt_sightseeing,swt_nightlife,swt_restaurant;
    Button btn_planTrip;
    EditText et_location;
    
    //
    Boolean isSightSeeing,isNightLife,isRestaurants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        et_location = findViewById(R.id.et_location);
        btn_planTrip = findViewById(R.id.btn_planTrip);
        swt_nightlife = findViewById(R.id.swt_nightlife);
        swt_restaurant = findViewById(R.id.swt_restaurant);
        swt_sightseeing = findViewById(R.id.swt_sightseeing);

        isSightSeeing = false;
        isNightLife = false;
        isRestaurants = false;
        

        img_royalcarribean.setOnClickListener(this);
        btn_planTrip.setOnClickListener(this);
        swt_sightseeing.setOnCheckedChangeListener(this);
        swt_restaurant.setOnCheckedChangeListener(this);
        swt_nightlife.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(TripPlannerActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();
        }else if(view == btn_planTrip)
        {
            if(!isNightLife && !isRestaurants && !isSightSeeing)
            {
                Toast.makeText(this, "please choose at least 1 activity", Toast.LENGTH_SHORT).show();
            }else{
                if(et_location.getText().toString().equals(""))
                {
                    Toast.makeText(this, "please type a location", Toast.LENGTH_SHORT).show();
                }else{

                        Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                           // getLongAndLat(et_location.getText().toString());
                            Amadeus amadeus = Amadeus
                                    .builder("UNsEf8gOfR76Xk4hIFdbREVwPHRQFdyk", "jURdf96v6iemuPBy")
                                    .build();
                            PointOfInterest[] pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                                    .with("latitude", "41.39715")
                                    .and("longitude", "2.160873")
                                    .and("category","SIGHTS"));
                            Log.d("TAG", "points of interest:"+pointsOfInterest.toString());

                            } catch (ResponseException e) {
                                e.printStackTrace();
                            }

                            }
                        });

                        thread.start();



                }
            }
        }
    }

    double lat;
    double lng;
    public void getLongAndLat(String locationInput)
    {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        try {
            address = coder.getFromLocationName(locationInput, 5);

            Address location = address.get(0);
            lat = location.getLatitude();
            lng = location.getLongitude();
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    
    
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton == swt_sightseeing)
        {
            if(b)
            {
                isSightSeeing = true;
            }else{
                isSightSeeing = false;
            }
        }else if(compoundButton == swt_nightlife){
            if(b)
            {
                isNightLife = true;
            }else{
                isNightLife = false;
            }
        }else if(compoundButton == swt_restaurant)
        {
            if(b)
            {
                isRestaurants = true;
            }else{
                isRestaurants = false;
            }
        }
    }
}