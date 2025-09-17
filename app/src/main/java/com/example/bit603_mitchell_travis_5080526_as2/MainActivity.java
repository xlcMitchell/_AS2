package com.example.bit603_mitchell_travis_5080526_as2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        locationPermissionRequest = PermissionsHelper.requestLocationPermissions(
                this,
                () -> { /* precise */ },
                () -> { /* approximate */ },
                () -> { /* denied */ }
        );

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted to execute code here

        } else {
            // Not granted so request launched
            locationPermissionRequest.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        //---Buttons---//
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnLocate = findViewById(R.id.btnLocate);
        Button btnStepCounter = findViewById(R.id.btnStepCounter);

        //---BUTTON ONCLICK LISTENERS---//
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO start profile activity
            }
        });

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,LocateMeActivity.class);
               startActivity(intent);
            }
        });

        btnStepCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StepCounterActivity.class);
                startActivity(intent);
            }
        });
    }


    }
