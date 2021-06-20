package com.acebrico.royalcarribeanapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.function.Consumer;

public class TripSummaryActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView img_royalcarribean;
    TextView tv_startPoint,tv_endPoint,tv_distanceToEndPoint;
    ListView lv_locations;
    Button btn_save;

    ArrayList<LocationAttraction> chosenAttractions;
    //
    ArrayList<LocationAttraction> findClosestToLastSightArray;
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

        chosenAttractions = new ArrayList<>();

        Log.d("TAG", TemporaryVariables.tostring());
        if(TemporaryVariables.chosenSightSeeingAttractions.size() > 0){
            TemporaryVariables.chosenSightSeeingAttractions = sortAttractions(TemporaryVariables.chosenSightSeeingAttractions);
            TemporaryVariables.chosenSightSeeingAttractions = updateDistances(TemporaryVariables.chosenSightSeeingAttractions);
            Log.d("TAG", "sight locations size:"+TemporaryVariables.chosenSightSeeingAttractions.size());

            TemporaryVariables.chosenSightSeeingAttractions.forEach(new Consumer<LocationAttraction>() {
                @Override
                public void accept(LocationAttraction locationAttraction) {
                    chosenAttractions.add(locationAttraction);
                }
            });
        }
        if(TemporaryVariables.chosenNightLifeAttractions.size() > 0)
        {
            TemporaryVariables.chosenNightLifeAttractions = sortAttractions(TemporaryVariables.chosenNightLifeAttractions);
            if(TemporaryVariables.chosenSightSeeingAttractions.size()>0)
            {
                findClosestToLastSightArray = new ArrayList<>();
                findClosestToLastSightArray.add(TemporaryVariables.chosenSightSeeingAttractions.get(TemporaryVariables.chosenSightSeeingAttractions.size()-1));
                TemporaryVariables.chosenNightLifeAttractions.forEach(new Consumer<LocationAttraction>() {
                    @Override
                    public void accept(LocationAttraction locationAttraction) {
                        findClosestToLastSightArray.add(locationAttraction);
                    }
                });
                findClosestToLastSightArray = updateDistances(findClosestToLastSightArray);
                findClosestToLastSightArray.remove(0);
                TemporaryVariables.chosenNightLifeAttractions = findClosestToLastSightArray;
            }else{
                TemporaryVariables.chosenNightLifeAttractions = updateDistances(TemporaryVariables.chosenNightLifeAttractions);
            }
            TemporaryVariables.chosenNightLifeAttractions.forEach(new Consumer<LocationAttraction>() {
                @Override
                public void accept(LocationAttraction locationAttraction) {
                    chosenAttractions.add(locationAttraction);
                }
            });
        }



        Log.d("TAG", "locations size:"+chosenAttractions.size());
        if(TemporaryVariables.isRestaurantsChosen)
        {
            ProgressDialog progressDialog = new ProgressDialog(TripSummaryActivity.this);
            progressDialog.setTitle("Loading Restaurants");
            progressDialog.show();
            PointOfInterest[] pointsOfInterest;

            ArrayList<LocationAttraction> restaurants = new ArrayList<>();
            Amadeus amadeus = Amadeus
                    .builder("xzDnM1eqDw4TlRIjAdqQ4LOxbZ015ida", "OBXSABf3MkqJD2ob")
                    .build();
            try {
                pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                        .with("latitude", TemporaryVariables.startPointLat)
                        .and("longitude", TemporaryVariables.startPointLng)
                        .and("category", "RESTAURANT"));

            } catch (ResponseException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(this, "not found restaurants", Toast.LENGTH_SHORT).show();
                return;
            }
            for (PointOfInterest point : pointsOfInterest) {
                if (point != null) {
                    //Log.d("TAG", "point:" + point.toString());
                    LocationAttraction templocation = new LocationAttraction();
                    templocation.name = point.getName();
                    templocation.lat = point.getGeoCode().getLatitude();
                    templocation.lng = point.getGeoCode().getLongitude();

                    restaurants.add(templocation);
                }
            }
            chosenAttractions.forEach(new Consumer<LocationAttraction>() {
                @Override
                public void accept(LocationAttraction locationAttraction) {
                    restaurants.forEach(new Consumer<LocationAttraction>() {
                        @Override
                        public void accept(LocationAttraction restaurant) {
                            restaurant.distanceFromCurrentPlace = distanceBetweenTwoLocations(locationAttraction,restaurant);
                        }
                    });
                    Collections.sort(restaurants, new Comparator<LocationAttraction>() {
                        @Override
                        public int compare(LocationAttraction restaurant1, LocationAttraction restaurant2) {
                            Double temp1 = restaurant1.distanceFromCurrentPlace;
                            Double temp2 = restaurant2.distanceFromCurrentPlace;
                            return temp1.compareTo(temp2);
                        }
                    });
                    locationAttraction.restaurantName1 = restaurants.get(0).name;
                    locationAttraction.restaurantName2 = restaurants.get(1).name;
                    restaurants.remove(0);
                    restaurants.remove(1);

                }
            });
            progressDialog.dismiss();

        }


        Log.d("TAG", "chosen attractions end:"+chosenAttractions);
        LocationSummaryAdapter locationSummaryAdapter = new LocationSummaryAdapter(
                chosenAttractions,TripSummaryActivity.this);
        lv_locations.setAdapter(locationSummaryAdapter);
        //
        img_royalcarribean.setOnClickListener(this);
        btn_save.setOnClickListener(this);

    }
    public ArrayList<LocationAttraction> sortAttractions(ArrayList<LocationAttraction> locations)
    {

        Collections.sort(locations, new Comparator<LocationAttraction>() {
            @Override
            public int compare(LocationAttraction locationAttraction, LocationAttraction t1) {
                Double temp1 = locationAttraction.distanceFromCurrentPlace;
                Double temp2 = t1.distanceFromCurrentPlace;
                return temp1.compareTo(temp2);
            }
        });

        Log.d("TAG", "locations now:"+locations.toString());

        return locations;
        //Collections.reverse(TemporaryVariables.chosenAttractions);
    }


    public ArrayList<LocationAttraction> updateDistances(ArrayList<LocationAttraction> locations)
    {

        if (locations.size() == 2) {
            locations.get(1).distanceFromCurrentPlace = distanceBetweenTwoLocations(
                    locations.get(0), locations.get(1));
        }else if(locations.size() == 3) {
            ArrayList<LocationAttraction> tempList = new ArrayList<>();
            LocationAttraction location0 = locations.get(0);
            LocationAttraction location1 = locations.get(1);
            LocationAttraction location2 = locations.get(2);
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

            locations = tempList;
        }else if(locations.size() == 4){
            locations = sortByDistance(locations.get(0)
                    ,locations,new ArrayList<>());
            Log.d("TAG", "updated locations 2:"+locations.size());
        }

        String distanceFromLastLocation = distance(
                TemporaryVariables.startPointLat, TemporaryVariables.startPointLng,
                locations.get(locations.size() - 1).lat,
                locations.get(locations.size() - 1).lng) + "";
        tv_distanceToEndPoint.setText("Distance:"+distanceFromLastLocation.substring(0,4) +"KM");

        return locations;
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
            Log.d("TAG", "updated locations:"+updatedLocations);
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