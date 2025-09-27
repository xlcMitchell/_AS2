package com.example.bit603_mitchell_travis_5080526_as2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bit603_mitchell_travis_5080526_as2.DataModel.AppDatabase;
import com.example.bit603_mitchell_travis_5080526_as2.DataModel.UserFitness;
import com.example.bit603_mitchell_travis_5080526_as2.DataModel.UserFitnessDao;
import com.example.bit603_mitchell_travis_5080526_as2.DataModel.Users;
import com.example.bit603_mitchell_travis_5080526_as2.DataModel.UsersDao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterUser extends AppCompatActivity {

    AppDatabase appDatabase;
    UsersDao usersDao;
    UserFitnessDao userFitnessDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_user);

        //creating database instance
        appDatabase = AppDatabase.createDatabaseInstance(this);
        //getting the student DAO interface to perform CRUD operations
        usersDao = appDatabase.usersDao();
        userFitnessDao = appDatabase.userFitnessDao();

        // --- EditTextViews ---
        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputFirstName = findViewById(R.id.inputFirstName);
        EditText inputLastName = findViewById(R.id.inputLastName);
        EditText inputAge = findViewById(R.id.inputAge);
        EditText inputGoal = findViewById(R.id.inputGoal);

       // --- RadioGroup and RadioButtons ---
        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        RadioButton radioMale = findViewById(R.id.radioMale);
        RadioButton radioFemale = findViewById(R.id.radioFemale);

       // --- Save button ---
        Button btnSaveProfile = findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save user to database and shared preference
                String email = inputEmail.getText().toString().trim();
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                String ageStr = inputAge.getText().toString().trim();
                String goal = inputGoal.getText().toString().trim();

                // --- Get selected gender ---
                int selectedId = genderGroup.getCheckedRadioButtonId();
                Boolean gender = null;
                if(selectedId != -1){ //make sure an option is selected
                    RadioButton selectedRadio = findViewById(selectedId);
                    if(selectedRadio.getText().toString().equals("Male")){
                        gender = true;
                    }else{
                        gender = false;
                    }
                }


                if(email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || ageStr.isEmpty()|| gender == null || goal.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date date = new Date();
                    SharedPreferences prefs = getSharedPreferences("mySteps", MODE_PRIVATE);
                    prefs.edit()
                            .putString("email", email)
                            .putString("firstName", firstName)
                            .putString("lastName", lastName)
                            .putInt("age", Integer.parseInt(ageStr))
                            .putBoolean("gender", gender)
                            .putInt("goal", Integer.parseInt(goal))
                            .apply();
                    try{
                        //TODO ADD  USER FITNESS
                        Users user = new Users(email,firstName,lastName,Integer.parseInt(ageStr),gender,Integer.parseInt(goal));
                        UserFitness userFitness = new UserFitness(date,email,0);
                        long id = usersDao.insertUsers(user);
                        long id1 = userFitnessDao.insertUserFitness(userFitness);
                        if(id != -1 && id1 != -1){
                            Toast.makeText(getApplicationContext(),"User added!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }
                }
            }
        });


    }
}