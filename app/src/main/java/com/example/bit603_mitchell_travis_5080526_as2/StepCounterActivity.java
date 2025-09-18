package com.example.bit603_mitchell_travis_5080526_as2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    private float totalSteps;
    private int previousTotalSteps;
    TextView stepsTaken,dailyStepCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_counter);

        SharedPreferences stepPref = getSharedPreferences("mySteps", MODE_PRIVATE);
        previousTotalSteps = stepPref.getInt("lastSteps",0);
        Log.d("STEPDEBUGGING", "previousTotalSteps = " + previousTotalSteps);

        stepsTaken = findViewById(R.id.stepsTaken);
        dailyStepCount = findViewById(R.id.dailyStepCount);
        stepsTaken.setText(String.valueOf(previousTotalSteps));


        //#---CHECK PERMISSION---#//
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor == null){
            Toast.makeText(this,"No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Sensor detected", Toast.LENGTH_SHORT).show();
            manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        totalSteps = event.values[0];
        Log.d("STEPDEBUGGING","Total Steps=" + totalSteps);
        int currentSteps = (int) totalSteps - previousTotalSteps;
        stepsTaken.setText(String.valueOf(currentSteps));
        // Save steps to shared preference
        SharedPreferences stepPref = getSharedPreferences("mySteps", MODE_PRIVATE);
        stepPref.edit().putInt("lastSteps", (int) totalSteps).apply();

        //check for new date
        checkNewDate((int) totalSteps);
        SharedPreferences startingStepsPref = getSharedPreferences("start",MODE_PRIVATE);
        int startingStep = startingStepsPref.getInt("start",0);
        int currentDailySteps = (int) totalSteps - startingStep;
        dailyStepCount.setText(String.valueOf(currentDailySteps));

    }
    private void checkNewDate(int total){
        SharedPreferences stepPref = getSharedPreferences("mySteps",MODE_PRIVATE);
        String savedDate = stepPref.getString("date","");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if(!savedDate.equals(currentDate)){
            stepPref.edit()
                    .putInt("start",total)
                    .putString("date",currentDate)
                    .apply();
        }
    }
}