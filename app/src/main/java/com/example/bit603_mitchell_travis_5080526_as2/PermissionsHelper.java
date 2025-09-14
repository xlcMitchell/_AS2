package com.example.bit603_mitchell_travis_5080526_as2;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionsHelper {
    public static ActivityResultLauncher<String[]> requestLocationPermissions(
            AppCompatActivity activity,
            Runnable onPreciseGranted,
            Runnable onApproximateGranted,
            Runnable onDenied) {

        return activity.registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    boolean fine = Boolean.TRUE.equals(
                            result.get(Manifest.permission.ACCESS_FINE_LOCATION));
                    boolean coarse = Boolean.TRUE.equals(
                            result.get(Manifest.permission.ACCESS_COARSE_LOCATION));

                    if (fine && coarse) {
                        onPreciseGranted.run();
                    } else if (coarse) {
                        onApproximateGranted.run();
                    } else {
                        onDenied.run();
                    }
                }
        );
    }
}


