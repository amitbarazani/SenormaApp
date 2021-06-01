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

public class LocationAttractionAdapter extends ArrayAdapter<LocationAttraction> {


    private ArrayList<LocationAttraction> locations;
    Context mContext;




    public LocationAttractionAdapter(ArrayList<LocationAttraction> data, Context context){
        super(context, R.layout.item_sightseeing, data);
        this.locations = data;
        this.mContext=context;

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

        if(locationAttraction.isOpen)
            tv_isOpen.setText("Open");
        else
            tv_isOpen.setText("Closed");
        tv_distance.setText("distance:..");
        //loadPicture(img_locationPic,locationAttraction.pictureUrl);
        cb_isLocationChosen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        viewGroup = parent;

        // Return the completed view to render on screen
        return convertView;
    }



    private void loadPicture(final ImageView img_profilePic, final String pictureUrl)
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        java.net.URL url = new java.net.URL(pictureUrl);
                        HttpURLConnection connection = (HttpURLConnection) url
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        img_profilePic.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("ERRORRRRR", "error loaded picture");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
