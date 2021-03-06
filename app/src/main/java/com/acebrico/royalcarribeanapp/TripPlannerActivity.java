package com.acebrico.royalcarribeanapp;

import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
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


import java.io.Serializable;
import java.util.List;

public class TripPlannerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageView img_royalcarribean;
    Switch swt_sightseeing,swt_nightlife,swt_restaurant;
    Button btn_planTrip;
    EditText et_location;
    
    //
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        et_location = findViewById(R.id.et_location);
        btn_planTrip = findViewById(R.id.btn_planTrip);
        swt_nightlife = findViewById(R.id.swt_nightlife);
        swt_restaurant = findViewById(R.id.swt_restaurant);
        swt_sightseeing = findViewById(R.id.swt_sightseeing);

        TemporaryVariables.isNightLifeChosen = false;
        TemporaryVariables.isSightSeeingChosen = false;
        TemporaryVariables.isRestaurantsChosen = false;



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
            if(!TemporaryVariables.isNightLifeChosen && !TemporaryVariables.isRestaurantsChosen && !TemporaryVariables.isSightSeeingChosen)
            {
                Toast.makeText(this, "please choose at least 1 activity", Toast.LENGTH_SHORT).show();
            }else{
                if(et_location.getText().toString().equals(""))
                {
                    Toast.makeText(this, "please type a location", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = new ProgressDialog(TripPlannerActivity.this);
                    progressDialog.setTitle("getting latitude and longitude...");
                    progressDialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                getLongAndLat(et_location.getText().toString());

                            } catch (Exception e) {
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
            if(address.size() > 0)
            {
                progressDialog.dismiss();
                progressDialog.cancel();
                TemporaryVariables.startPointLat = lat;
                TemporaryVariables.startPointLng = lng;
                TemporaryVariables.startPointName = et_location.getText().toString();
                if(TemporaryVariables.isSightSeeingChosen) {
                    Intent intent = new Intent(TripPlannerActivity.this, ShowSightSeeingActivity.class);
                    startActivity(intent);
                    finish();
                }else if(!TemporaryVariables.isRestaurantsChosen && TemporaryVariables.isNightLifeChosen)
                {
                    Intent intent = new Intent(TripPlannerActivity.this, ShowNightLifeActivity.class);
                    startActivity(intent);
                    finish();
                }else if(TemporaryVariables.isRestaurantsChosen && !TemporaryVariables.isNightLifeChosen)
                {
                    Intent intent = new Intent(TripPlannerActivity.this, ShowRestaurantsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(TemporaryVariables.isRestaurantsChosen)
                {
                    Intent intent = new Intent(TripPlannerActivity.this, ShowNightLifeActivity.class);
                    startActivity(intent);
                    finish();
                }


            }else{
                Toast.makeText(this, "couldn't find place...", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
           
            ex.printStackTrace();
            TripPlannerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Toast.makeText(TripPlannerActivity.this, "wifi problem: try to turn on and off your airplane mode", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    
    
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton == swt_sightseeing)
        {
            if(b)
            {
                TemporaryVariables.isSightSeeingChosen = true;
            }else{
                TemporaryVariables.isSightSeeingChosen = false;
            }
        }else if(compoundButton == swt_nightlife){
            if(b)
            {
                TemporaryVariables.isNightLifeChosen = true;
            }else{
                TemporaryVariables.isNightLifeChosen = false;
            }
        }else if(compoundButton == swt_restaurant)
        {
            if(b)
            {
                TemporaryVariables.isRestaurantsChosen = true;
            }else{
                TemporaryVariables.isRestaurantsChosen = false;
            }
        }
    }
}