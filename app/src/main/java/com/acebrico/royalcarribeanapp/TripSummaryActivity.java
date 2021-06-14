package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TripSummaryActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img_royalcarribean;
    TextView tv_startPoint,tv_endPoint;
    ListView lv_locations;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_summary);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_save = findViewById(R.id.btn_save);
        tv_endPoint = findViewById(R.id.tv_endPoint);
        tv_startPoint = findViewById(R.id.tv_startingPoint);
        lv_locations = findViewById(R.id.lv_locations);
        //
        LocationSummaryAdapter locationSummaryAdapter = new LocationSummaryAdapter(
                TemporaryVariables.chosenAttractions,TripSummaryActivity.this);
        lv_locations.setAdapter(locationSummaryAdapter);
        //
        img_royalcarribean.setOnClickListener(this);
        btn_save.setOnClickListener(this);

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
}