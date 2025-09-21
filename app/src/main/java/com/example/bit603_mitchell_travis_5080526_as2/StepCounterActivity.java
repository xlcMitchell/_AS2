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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;
    TextView goalComplete,realTimeText,goalText;
    int realTimeSteps,goal,currentDailySteps,stepsSinceAppStart;
    String weeklySteps;

    ProgressBar progress;
    CircularProgressBar circleProgress;

    int dailyStepTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_counter);

        realTimeText = findViewById(R.id.realTimeSteps);
        circleProgress = findViewById(R.id.circularProgressBar);
        goalComplete = findViewById(R.id.goalComplete);
        goalText = findViewById(R.id.goalText);

        //Update daily steps and real time steps to latest known value
        SharedPreferences prefs = getSharedPreferences("mySteps",MODE_PRIVATE);
        realTimeSteps = prefs.getInt("dailyTotal",0);
        weeklySteps = prefs.getString("weeklySteps","500,1000,1200,1400,1700,800,900");
        int [] weeklyStepsArray = stringToArray(weeklySteps);
        realTimeText.setText(String.valueOf(realTimeSteps));
        stepsSinceAppStart = 0;
        newDate(); //update real time steps
        goal = 1000;
        goalText.setText("Goal: " + String.valueOf(goal) + "steps");


        circleProgress.setProgressMax(goal); // Goal
        circleProgress.setProgress(realTimeSteps); // current steps
        circleProgress.setProgressBarWidth(20f);
        circleProgress.setBackgroundProgressBarWidth(12f);
        circleProgress.setProgressBarColor(Color.GREEN);
        circleProgress.setBackgroundProgressBarColor(Color.GRAY);

        populateChart(weeklyStepsArray,prefs.getInt("index",0)); //add past 7 days to chart

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
            manager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
        //#---END OF SENSOR MANAGER---#//


    }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //----Logic for the TYPE_STEP_COUNTER----//
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            Log.d("STEP_COUNTER","TYPE_STEP_COUNTER");
            float totalSteps = event.values[0]; //Retrieve total steps since last reboot
            checkNewDate((int) totalSteps);  //Set new starting point if it is a new day
            SharedPreferences startingStepsPref = getSharedPreferences("mySteps",MODE_PRIVATE);
            int startingStep = startingStepsPref.getInt("start",0);
            currentDailySteps = (int) totalSteps - startingStep;
            checkGoalReached();
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

            //----Logic for the TYPE_STEP_DETECTOR----//
        }else if(event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
                realTimeSteps++;
                stepsSinceAppStart++;
                realTimeText.setText(String.valueOf(realTimeSteps));
                checkGoalReached();
        }
    }
    private void checkNewDate(int total){
        SharedPreferences stepPref = getSharedPreferences("mySteps",MODE_PRIVATE);
        String savedDate = stepPref.getString("date","");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if(!savedDate.equals(currentDate)){
                updateArray(); //update the array to store the last days steps
                stepPref.edit()
                        .putInt("start",total - stepsSinceAppStart)
                        .putString("date",currentDate)
                        .putBoolean("msgShow",false)
                        .putInt("real",0)
                        .apply();
            realTimeSteps = 0; //reset steps in real time
            }
    }

    //function to update real time steps when  user first opens app on a new day
    private void newDate(){
        SharedPreferences stepPref = getSharedPreferences("mySteps",MODE_PRIVATE);
        String savedDate = stepPref.getString("date","");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if(!savedDate.equals(currentDate)){
            realTimeSteps = 0; //reset steps in real time
        }
    }

    private void checkGoalReached(){
        if(currentDailySteps >= goal || realTimeSteps >= goal){
            goalComplete.setText("Congratulations! Goal Reached");
        }
    }

    //Function to convert int array to string for SharedPrefs
    private String arrayToString(int [] array){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < array.length; i++){
            sb.append(array[i]);
            if(i < array.length - 1)
                sb.append(","); //add a comma in between except last value
        }
        return sb.toString();

    }

    private int [] stringToArray(String steps){
        String [] dailySteps = steps.split(",");
        int [] arr = new int[dailySteps.length];
        for(int i=0; i < dailySteps.length; i++){
            int val = Integer.parseInt(dailySteps[i]);
            arr[i] = val;

        }
        return arr;
    }


    private void updateArray(){
        //Retrieve index to track current day from sharedPreference
        SharedPreferences prefs = getSharedPreferences("mySteps",MODE_PRIVATE);
        int currentIndex = prefs.getInt("index",0);
        int newIndex = (currentIndex + 1) % 7; //should increase to 7 and go back to 0
        String arrString = prefs.getString("weeklySteps","500,1000,1200,1400,1700,800,900");
        int [] intArr = stringToArray(arrString);
        intArr[currentIndex] = currentDailySteps;
        prefs.edit()
                .putString("weeklySteps",arrayToString(intArr))
                .putInt("index",newIndex)
                .apply();

    }

    private void populateChart(int[] weeklySteps, int index) {
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        // Today’s slot in the circular buffer because index is one day ahead
        //adding weeklysteps.length ensures number stays positive
        int todayIndex = (index - 1 + weeklySteps.length) % weeklySteps.length;

        // Fill bars in chronological order, ending at today
        for (int i = 0; i < weeklySteps.length; i++) {
            int arrayIndex = (todayIndex - (weeklySteps.length - 1 - i) + weeklySteps.length) % weeklySteps.length;
            entries.add(new BarEntry(i, weeklySteps[arrayIndex]));

            // Label last bar as Today
            if (i == weeklySteps.length - 1) {
                labels.add("Today");
            } else {
                labels.add("");
            }
        }

        BarDataSet dataSet = new BarDataSet(entries, "Steps");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // Format X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}