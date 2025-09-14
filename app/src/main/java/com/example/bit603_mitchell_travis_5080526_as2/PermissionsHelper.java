package com.example.bit603_mitchell_travis_5080526_as2;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionsHelper {
    private final AppCompatActivity activity;

    public PermissionsHelper(AppCompatActivity activity){
        this.activity = activity;
    }

    public ActivityResultLauncher<String[]> requestLocationPermissions(
            Runnable onPreciseGranted,
            Runnable onApproximateGranted,
            Runnable onDenied
    ){
        return activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION,false);
                    boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION,false);

                    if (Boolean.TRUE.equals(fineLocationGranted) &&
                            Boolean.TRUE.equals(coarseLocationGranted)) {
                        onPreciseGranted.run();
                    } else if (Boolean.TRUE.equals(coarseLocationGranted)) {
                        onApproximateGranted.run();
                    } else {
                        onDenied.run();
                    }

                }
        );
    }

}
