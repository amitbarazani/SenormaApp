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
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;


public class LocationSummaryAdapter extends ArrayAdapter<LocationAttraction> {


    private ArrayList<LocationAttraction> locations;
    Context mContext;
    //ArrayList<LocationAttraction> chosenAttractions;



    public LocationSummaryAdapter(ArrayList<LocationAttraction> data, Context context){
        super(context, R.layout.item_location_summary, data);
        this.locations = data;
        this.mContext=context;
        // this.chosenAttractions = new ArrayList<>();


    }

    ViewGroup viewGroup;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final LocationAttraction locationAttraction = getItem(position);

        final View result;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_location_summary, parent, false);
            result=convertView;
        }

        TextView tv_name = convertView.findViewById(R.id.tv_locationName);
        TextView tv_description = convertView.findViewById(R.id.tv_description);
        TextView tv_distance = convertView.findViewById(R.id.tv_distance);
        RelativeLayout rl_restaurants = convertView.findViewById(R.id.rl_restaurants);
        TextView tv_restaurantName1 = convertView.findViewById(R.id.tv_restaurantName1);
        TextView tv_restaurantName2 = convertView.findViewById(R.id.tv_restaurantName2);
        ImageView img_locationPic = convertView.findViewById(R.id.img_locationPic);

        tv_name.setText(locationAttraction.name);
        tv_description.setText(locationAttraction.description);
        tv_distance.setText("Distance:"+locationAttraction.distanceFromCurrentPlace.toString().substring(0,4));
        img_locationPic.setImageBitmap(locationAttraction.pictureBitmap);
        if(TemporaryVariables.isRestaurantsChosen)
        {
            rl_restaurants.setVisibility(View.VISIBLE);
            tv_restaurantName1.setText(locationAttraction.restaurantName1);
            tv_restaurantName2.setText(locationAttraction.restaurantName2);
        }else{
            rl_restaurants.setVisibility(View.GONE);
        }

        viewGroup = parent;

        // Return the completed view to render on screen
        return convertView;
    }




}

