package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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
        updateDistances();

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

        Log.d("TAG", "attractions now:"+TemporaryVariables.chosenAttractions.toString());

        //Collections.reverse(TemporaryVariables.chosenAttractions);
    }


    public void updateDistances()
    {

        if (TemporaryVariables.chosenAttractions.size() == 2) {
            TemporaryVariables.chosenAttractions.get(1).distanceFromCurrentPlace = distanceBetweenTwoLocations(
                    TemporaryVariables.chosenAttractions.get(0), TemporaryVariables.chosenAttractions.get(1));
        }else if(TemporaryVariables.chosenAttractions.size() == 3) {
            ArrayList<LocationAttraction> tempList = new ArrayList<>();
            LocationAttraction location0 = TemporaryVariables.chosenAttractions.get(0);
            LocationAttraction location1 = TemporaryVariables.chosenAttractions.get(1);
            LocationAttraction location2 = TemporaryVariables.chosenAttractions.get(2);
            if(distanceBetweenTwoLocations(location0,location1) < distanceBetweenTwoLocations(location0,location2))
            {
                tempList.add(location0);
                tempList.add(location1);
                tempList.add(location2);
            }else {
                tempList.add(location0);
                tempList.add(location2);
                tempList.add(location1);
            }

            TemporaryVariables.chosenAttractions = tempList;
        }else if(TemporaryVariables.chosenAttractions.size() == 4){
            TemporaryVariables.chosenAttractions = sortByDistance(TemporaryVariables.chosenAttractions.get(0)
                    ,TemporaryVariables.chosenAttractions,new ArrayList<>());
        }

        String distanceFromLastLocation = distance(
                TemporaryVariables.startPointLat, TemporaryVariables.startPointLng,
                TemporaryVariables.chosenAttractions.get(TemporaryVariables.chosenAttractions.size() - 1).lat,
                TemporaryVariables.chosenAttractions.get(TemporaryVariables.chosenAttractions.size() - 1).lng) + "";
        tv_distanceToEndPoint.setText("Distance:"+distanceFromLastLocation.substring(0,4) +"KM");

    }




    public ArrayList<LocationAttraction> sortByDistance(LocationAttraction tempAttraction
            ,ArrayList<LocationAttraction> locations,ArrayList<LocationAttraction> updatedLocations)
    {
        Log.d("TAG", "locations size:"+locations.size());
        if(locations.size() == 1)
        {
            LocationAttraction tempAttraction1 = locations.get(0);
            updatedLocations.add(tempAttraction1);
            locations.remove(0);
            return updatedLocations;
        }
        if(updatedLocations.size() == 0)
        {
            updatedLocations.add(tempAttraction);
            locations.remove(0);
        }

        Double min = 10000000.0;
        int minIndex = -1;
        for (int i = 0;i<locations.size();i++)
        {
            if(tempAttraction != locations.get(i)) {
                if (distanceBetweenTwoLocations(tempAttraction,locations.get(i)) < min) {
                    min = distanceBetweenTwoLocations(tempAttraction,locations.get(i));
                    minIndex = i;
                    Log.d("TAG", "name of min attraction:"+locations.get(i).name);
                }
            }
        }
        Log.d("TAG", "min Index:"+minIndex);
        LocationAttraction tempAttraction1 = locations.get(minIndex);
        updatedLocations.add(tempAttraction1);
        locations.remove(minIndex);
        Log.d("TAG", "locations:"+locations.toString());
        return sortByDistance(tempAttraction1,locations,updatedLocations);

    }


    public Double distanceBetweenTwoLocations(LocationAttraction location1,LocationAttraction location2)
    {
        return distance(location1.lat,location1.lng,location2.lat,location2.lng);
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
            saveScreenshot();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity( new Intent(TripSummaryActivity.this,TripPlannerActivity.class));
        finish();
    }


    //TODO: not working, needs to be fixed
    private void saveScreenshot() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},00);

        String filename;
        Date date = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat ("ddMMyyyyHHmmss");
        filename =  sdf.format(date);

        try{
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, "/Download/"+filename+".jpg");
            fOut = new FileOutputStream(file);

            takeScreenshot().compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(getContentResolver()
                    ,file.getAbsolutePath(),file.getName(),file.getName());
            openScreenshot(file);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap takeScreenshot()
    {
        // create bitmap screen capture
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        return bitmap;
    }


    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(TripSummaryActivity.this,
                TripSummaryActivity.this.getApplicationContext().getPackageName() + ".provider", imageFile);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    

}