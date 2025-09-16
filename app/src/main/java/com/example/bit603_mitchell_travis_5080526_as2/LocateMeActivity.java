package com.example.bit603_mitchell_travis_5080526_as2;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bit603_mitchell_travis_5080526_as2.databinding.ActivityLocateMeBinding;

public class LocateMeActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private LocationManager locationManager;
    private FusedLocationProviderClient locationClient; //Class to get users last location
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private GoogleMap mMap;
    private ActivityLocateMeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLocateMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //---Back button click listener---//
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request permissions
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, // 5 seconds
                    1,   // 10 meters
                    this
            );
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //TODO add users location
        /*
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
         */
        //Android studio would like to check permission again before checking for last location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"You have not granted permission",Toast.LENGTH_LONG).show();
            return;
        }
                locationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) { //Make sure that last location is not null
                        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                        mMap.addMarker(new MarkerOptions()
                                .position(myLatLng)
                                .title("My location"));
                    } else {
                        // Last location is null
                        Toast.makeText(this, "Location not available yet", Toast.LENGTH_SHORT).show();
                        //Default location incase last known location is not available
                        LatLng defaultLocation = new LatLng(-36.8485, 174.7633);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
                    }
                });


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Current Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(myLocation));
    }
}