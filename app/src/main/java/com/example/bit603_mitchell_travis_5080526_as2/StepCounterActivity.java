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
    TextView dailyStepCount,realTimeText;
    int realTimeSteps;

    int dailyStepTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_counter);

        dailyStepCount = findViewById(R.id.dailyStepCount);
        realTimeText = findViewById(R.id.realTimeSteps);


        //#---CHECK PERMISSION---#//
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }
        }

        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor stepDetector = manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        manager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
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
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            float totalSteps = event.values[0]; //Retrieve total steps since last reboot
            Log.d("STEPDEBUGGING","Total Steps=" + totalSteps);
            //check for new date and create a new starting point if it is a new day
            checkNewDate((int) totalSteps);
            SharedPreferences startingStepsPref = getSharedPreferences("mySteps",MODE_PRIVATE);
            int startingStep = startingStepsPref.getInt("start",0);
            int currentDailySteps = (int) totalSteps - startingStep;
            dailyStepCount.setText(String.valueOf(currentDailySteps));
        }else if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                Log.d("STEPDEBUGGING", "REAL TIME STEPS UPDATE");
                realTimeSteps++;
                realTimeText.setText(String.valueOf(realTimeSteps));

        }


    }
    private void checkNewDate(int total){
        SharedPreferences stepPref = getSharedPreferences("mySteps",MODE_PRIVATE);
        String savedDate = stepPref.getString("date","");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if(!savedDate.equals(currentDate)){
            stepPref.edit()
                    .putInt("start",total)
                    .putString("date",currentDate)
                    .putInt("real",0)
                    .apply();
            realTimeSteps = 0; //reset steps in real time
        }
    }
}