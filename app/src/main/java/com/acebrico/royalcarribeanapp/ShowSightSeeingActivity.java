package com.acebrico.royalcarribeanapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.PointOfInterest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowSightSeeingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_royalcarribean;
    ListView lv_locations;
    //
    PointOfInterest[] pointsOfInterest;
    ArrayList<LocationAttraction> locationAttractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sight_seeing);
        img_royalcarribean = findViewById(R.id.img_royalcarribean);
        lv_locations = findViewById(R.id.lv_locations);

        Double lat = getIntent().getDoubleExtra("lat", 0.0);
        Double lng = getIntent().getDoubleExtra("lng", 0.0);
        loadLocations(lat, lng);
        getAllPlaces();

        //
        img_royalcarribean.setOnClickListener(this);
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


        LocationAttractionAdapter locationAttractionAdapter = new LocationAttractionAdapter(locationAttractions, ShowSightSeeingActivity.this);
        lv_locations.setAdapter(locationAttractionAdapter);
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

    }

    public void getAllPlaces() {
        // Initialize the SDK
        Places.initialize(getApplicationContext(), "AIzaSyAlsDSqPYncPQDXhREqVsYgj6YiVGSyNMo");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();


        for (LocationAttraction locationAttraction:locationAttractions) {


            // Use the builder to create a FindAutocompletePredictionsRequest.
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ADDRESS)
                    .setSessionToken(token)
                    .setQuery(locationAttraction.name)
                    .build();

            placesClient.findAutocompletePredictions(request).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {

                    // Define a Place ID.
                    for (AutocompletePrediction response : task.getResult().getAutocompletePredictions()) {
                        Log.d("TAG", "place id:" + response.getPlaceId());
                    }


                    final String placeId = task.getResult().getAutocompletePredictions().get(0).getPlaceId();
                    final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                    final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);


                    placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                        Place place = response.getPlace();
                        Log.i("TAG", "Place found: " + place.getName());
                        locationAttraction.pictureUrl = place.getPhotoMetadatas().get(0).toString();


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
    }


    @Override
    public void onClick(View view) {
        if(view == img_royalcarribean)
        {
            Intent intent = new Intent(ShowSightSeeingActivity.this,MenuClientActivity.class);
            startActivity(intent);
            finish();
        }
    }
}