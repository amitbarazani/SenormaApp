package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class TripPlannerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageView img_royalcarribean;
    Switch swt_sightseeing,swt_nightlife,swt_restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_planner);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        swt_nightlife = findViewById(R.id.swt_nightlife);
        swt_restaurant = findViewById(R.id.swt_restaurant);
        swt_sightseeing = findViewById(R.id.swt_sightseeing);

        img_royalcarribean.setOnClickListener(this);
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
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton == swt_sightseeing)
        {
            if(b)
            {
                Toast.makeText(this, "chosen sight seeing", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "not chosen sight seeing", Toast.LENGTH_SHORT).show();
            }
        }else if(compoundButton == swt_nightlife){
            if(b)
            {
                Toast.makeText(this, "chosen night life", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "not chosen night life", Toast.LENGTH_SHORT).show();
            }
        }else if(compoundButton == swt_restaurant)
        {
            if(b)
            {
                Toast.makeText(this, "chosen restaurant", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "not chosen restaurant", Toast.LENGTH_SHORT).show();
            }
        }
    }
}