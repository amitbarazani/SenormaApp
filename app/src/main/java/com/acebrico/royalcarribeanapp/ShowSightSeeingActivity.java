package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowSightSeeingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean;
    ListView lv_locations;
    Button btn_calculate;
    //
    PointOfInterest[] pointsOfInterest;
    ArrayList<LocationAttraction> locationAttractions;

    Double latCurrentPlace;
    Double lngCurrentPlace;
    //
    Integer loadingPercent;
    ProgressDialog progressLoadingAttractions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sight_seeing);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        btn_calculate = findViewById(R.id.btn_calculate);
        lv_locations = findViewById(R.id.lv_locations);

        TemporaryVariables.chosenAttractions = new ArrayList<>();

        progressLoadingAttractions = new ProgressDialog(this);
        progressLoadingAttractions.setTitle("Loading attractions...");
        progressLoadingAttractions.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(ShowSightSeeingActivity.this, "canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        progressLoadingAttractions.show();

         latCurrentPlace = getIntent().getDoubleExtra("lat", 0.0);
         lngCurrentPlace = getIntent().getDoubleExtra("lng", 0.0);
        loadLocations(latCurrentPlace, lngCurrentPlace);

        //
        img_royalcarribean.setOnClickListener(this);
        btn_calculate.setOnClickListener(this);
    }


    public void loadLocations(Double lat, Double lng) {
        locationAttractions = new ArrayList<>();
        Amadeus amadeus = Amadeus
                .builder("xzDnM1eqDw4TlRIjAdqQ4LOxbZ015ida", "OBXSABf3MkqJD2ob")
                .build();
        try {
            pointsOfInterest = amadeus.referenceData.locations.pointsOfInterest.get(Params
                    .with("latitude", lat)
                    .and("longitude", lng)
                    .and("category", "SIGHTS"));
        } catch (ResponseException e) {
            e.printStackTrace();
            progressLoadingAttractions.dismiss();
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
            return;
        }
        for (PointOfInterest point : pointsOfInterest) {
            if (point != null) {
                Log.d("TAG", "point:" + point.toString());
                LocationAttraction templocation = new LocationAttraction();
                templocation.name = point.getName();
                templocation.lat = point.getGeoCode().getLatitude();
                templocation.lng = point.getGeoCode().getLongitude();

                locationAttractions.add(templocation);
            }
        }


        loadingPercent = 0;
        for(int i = 0;i<locationAttractions.size();i++) {
            getPlaceDetails(i);
        }


    }

//    Integer counter = 0;
    public void getPlaceDetails(Integer i) {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyAlsDSqPYncPQDXhREqVsYgj6YiVGSyNMo");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(locationAttractions.get(i).name)
                .build();

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
            @Override
            public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {

                // Define a Place ID.
                for (AutocompletePrediction response : task.getResult().getAutocompletePredictions()) {
                    Log.d("TAG", "place id:" + response.getPlaceId());
                }



                final String placeId = task.getResult().getAutocompletePredictions().get(0).getPlaceId();
                final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME
                        ,Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.PHOTO_METADATAS
                        ,Place.Field.OPENING_HOURS,Place.Field.RATING,Place.Field.TYPES,Place.Field.UTC_OFFSET);
                final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);



                placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();
                    Log.i("TAG", "Place found: " + place.getName() + ","+place.getRating()+","+place.getAddress()+","+place.isOpen()+","+place.getTypes());
                    if(place.isOpen() != null)
                        locationAttractions.get(i).isOpen = place.isOpen();

                    locationAttractions.get(i).description = place.getTypes().get(0).toString().toLowerCase().replace("_"," ");
                    locationAttractions.get(i).rating = place.getRating();
                    locationAttractions.get(i).lat = place.getLatLng().latitude;
                    locationAttractions.get(i).lng = place.getLatLng().longitude;
                    locationAttractions.get(i).distanceFromCurrentPlace = distance(locationAttractions.get(i).lat
                            ,locationAttractions.get(i).lng,latCurrentPlace,lngCurrentPlace);


                    // Get the photo metadata.
                    final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                    if (metadata == null || metadata.isEmpty()) {
                        Log.w("TAG", "No photo metadata.");
                        loadingPercent +=10;
                        progressLoadingAttractions.setTitle("Loaded "+loadingPercent+"%");
                        Log.d("TAG", "loaded attraction:"+locationAttractions.get(i).toString());
                        return;
                    }
                    final PhotoMetadata photoMetadata = metadata.get(0);

                    final String attributions = photoMetadata.getAttributions();

                    final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .build();
                    placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                        Bitmap pictureBitmap = fetchPhotoResponse.getBitmap();
                        locationAttractions.get(i).pictureBitmap = pictureBitmap;
                        Log.d("TAG", "location attraction:"+locationAttractions.get(i).toString());

                        if(i != locationAttractions.size()-1)
                        {
                            loadingPercent +=10;
                            progressLoadingAttractions.setTitle("Loaded "+loadingPercent+"%");
                            Log.d("TAG", "loaded attraction:"+locationAttractions.get(i).toString());
                        }else{

                            Collections.sort(locationAttractions, new Comparator<LocationAttraction>() {
                                @Override
                                public int compare(LocationAttraction locationAttraction, LocationAttraction t1) {
                                   Double temp1 = locationAttraction.rating / locationAttraction.distanceFromCurrentPlace;
                                   Double temp2 = t1.rating / t1.distanceFromCurrentPlace;
                                   return temp1.compareTo(temp2);
                                }
                            });
                            Collections.reverse(locationAttractions);
                            LocationAttractionAdapter locationAttractionAdapter = new LocationAttractionAdapter(locationAttractions, ShowSightSeeingActivity.this);
                            lv_locations.setAdapter(locationAttractionAdapter);
                            progressLoadingAttractions.dismiss();
                        }

                    }).addOnFailureListener((exception) -> {
                        if (exception instanceof ApiException) {
                            final ApiException apiException = (ApiException) exception;
                            Log.e("TAG", "Place not found: " + exception.getMessage());
                            final int statusCode = apiException.getStatusCode();
                            // TODO: Handle error with given status code.
                        }
                    });







                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        final ApiException apiException = (ApiException) exception;
                        Log.e("TAG", "Place not found: " + exception.getMessage());
                        final int statusCode = apiException.getStatusCode();
                        // TODO: Handle error with given status code.
                    }
                });
            }
        });



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
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ShowSightSeeingActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();
        }else if(view == btn_calculate)
        {
            if(TemporaryVariables.chosenAttractions.size() > 0 && TemporaryVariables.chosenAttractions.size() <= 4)
            {
                Intent intent = new Intent(ShowSightSeeingActivity.this,TripSummaryActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "please choose at least 1 activity and less then 4 activities.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}