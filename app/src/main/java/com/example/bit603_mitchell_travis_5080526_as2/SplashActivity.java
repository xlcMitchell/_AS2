package com.example.bit603_mitchell_travis_5080526_as2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds then move to next activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("mySteps", MODE_PRIVATE);
            boolean hasUser = prefs.contains("email");
            Intent intent;
            if (hasUser) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, RegisterUser.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }


}