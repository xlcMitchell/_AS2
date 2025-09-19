package com.example.bit603_mitchell_travis_5080526_as2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    TextView dailyStepCount,realTimeText;
    int realTimeSteps;

    ProgressBar progress;
    CircularProgressBar circleProgress;

    int dailyStepTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_counter);

        //dailyStepCount = findViewById(R.id.dailyStepCount);
        realTimeText = findViewById(R.id.realTimeSteps);
        //progress = findViewById(R.id.progressBarHorizontal);
        circleProgress = findViewById(R.id.circularProgressBar);

        //Update daily steps and real time steps to latest known value
        SharedPreferences prefs = getSharedPreferences("mySteps",MODE_PRIVATE);
        realTimeSteps = prefs.getInt("dailyTotal",0);
        realTimeText.setText(String.valueOf(realTimeSteps));
        //dailyStepCount.setText(String.valueOf(realTimeSteps));
        //progress.setProgress(realTimeSteps);

        circleProgress.setProgressMax(10000); // Goal
        circleProgress.setProgress(realTimeSteps); // current steps
        circleProgress.setProgressBarWidth(20f);
        circleProgress.setBackgroundProgressBarWidth(12f);
        circleProgress.setProgressBarColor(Color.GREEN);
        circleProgress.setBackgroundProgressBarColor(Color.GRAY);

        //#---CHECK PERMISSION---#//
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
            }

        //#---CREATE AND REGISTER SENSOR MANAGER---#//
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
        //#---END OF SENSOR MANAGER---#//


    }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            float totalSteps = event.values[0]; //Retrieve total steps since last reboot
            //check for new date and create a new starting point if it is a new day
            checkNewDate((int) totalSteps);
            SharedPreferences startingStepsPref = getSharedPreferences("mySteps",MODE_PRIVATE);
            int startingStep = startingStepsPref.getInt("start",0);
            int currentDailySteps = (int) totalSteps - startingStep;
            //dailyStepCount.setText(String.valueOf(currentDailySteps));
            //progress.setProgress(currentDailySteps); //update progress bar
            circleProgress.setProgressWithAnimation(currentDailySteps, 1000L);
            //If the steps are not up to date for example if we start the app and
            //it is counting from the previous step count because TYPE_STEP_COUNTER
            //doesn't update as frequently
            if(currentDailySteps != realTimeSteps){
                realTimeSteps = currentDailySteps;
            }
            //saving latest step count total
            startingStepsPref.edit()
                    .putInt("dailyTotal",currentDailySteps)
                    .apply();
        }else if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
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