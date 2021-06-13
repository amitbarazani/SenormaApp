package com.acebrico.royalcarribeanapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

public class LocationAttractionAdapter extends ArrayAdapter<LocationAttraction> {


    private ArrayList<LocationAttraction> locations;
    Context mContext;
    //ArrayList<LocationAttraction> chosenAttractions;



    public LocationAttractionAdapter(ArrayList<LocationAttraction> data, Context context){
        super(context, R.layout.item_sightseeing, data);
        this.locations = data;
        this.mContext=context;
       // this.chosenAttractions = new ArrayList<>();
        ShowSightSeeingActivity.chosenAttractions = new ArrayList<>();


    }

    ViewGroup viewGroup;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LocationAttraction locationAttraction = getItem(position);

        final View result;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_sightseeing, parent, false);
            result=convertView;
        }

        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_locationName);
        TextView tv_description = (TextView)convertView.findViewById(R.id.tv_description);
        TextView tv_isOpen = (TextView)convertView.findViewById(R.id.tv_isOpen);
        TextView tv_distance = (TextView)convertView.findViewById(R.id.tv_distance);
        TextView tv_rating = (TextView)convertView.findViewById(R.id.tv_rating);
        CheckBox cb_isLocationChosen = convertView.findViewById(R.id.cb_isLocationChosen);
        ImageView img_locationPic = (ImageView) convertView.findViewById(R.id.img_locationPic);

        tv_name.setText(locationAttraction.name);
        tv_description.setText(locationAttraction.description);
        tv_rating.setText("rating:"+locationAttraction.rating);

        if(locationAttraction.isOpen != null) {
            if(locationAttraction.isOpen)
                tv_isOpen.setText("Open");
            else
                tv_isOpen.setText("Closed");
        }

        tv_distance.setText("distance:"+locationAttraction.distanceFromCurrentPlace.toString().substring(0,4));
        img_locationPic.setImageBitmap(locationAttraction.pictureBitmap);


        cb_isLocationChosen.setOnCheckedChangeListener(null);
        cb_isLocationChosen.setFocusable(false);
        if (locations.get(position).isChosen) {
            cb_isLocationChosen.setChecked(true);
        } else {
            cb_isLocationChosen.setChecked(false);
        }
        cb_isLocationChosen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    ShowSightSeeingActivity.chosenAttractions.add(locationAttraction);
                    locations.get(position).isChosen = true;
                }else{
                    ShowSightSeeingActivity.chosenAttractions.remove(locationAttraction);
                    locations.get(position).isChosen = false;
                }
                Log.d("TAG", "chosen attractions:"+ShowSightSeeingActivity.chosenAttractions.toString());
            }
        });


        viewGroup = parent;

        // Return the completed view to render on screen
        return convertView;
    }




}
