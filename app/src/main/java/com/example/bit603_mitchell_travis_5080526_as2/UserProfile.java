package com.example.bit603_mitchell_travis_5080526_as2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

public class UserProfile extends AppCompatActivity {

    AppDatabase database;
    UsersDao usersDao;
    UserFitnessDao userFitnessDao;
    List<Users> user;
    List <UserFitness> userFitness;
    int index = -1;

    boolean editing = false;

    EditText editTextFirstName, editTextLastName, editTextAge, editTextGoal, editTextGender, editTextEmail;
    Button btnEdit, btnSave, btnDelete, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        editTextFirstName = findViewById(R.id.etFirstName);
        editTextLastName = findViewById(R.id.etLastName);
        editTextAge = findViewById(R.id.etAge);
        editTextGoal = findViewById(R.id.etGoal);
        editTextGender = findViewById(R.id.etGender);
        editTextEmail = findViewById(R.id.etEmail);

        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);

        database = AppDatabase.createDatabaseInstance(getApplicationContext());
        usersDao = database.usersDao();
        userFitnessDao = database.userFitnessDao();
        userFitness = userFitnessDao.readAllUserFitness();
        user = usersDao.readAllUsers();

        if(!user.isEmpty()){
            index = 0;
        }

        showRecord();


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFirstName.setEnabled(true);
                editTextLastName.setEnabled(true);
                editTextAge.setEnabled(true);
                editTextGoal.setEnabled(true);
                editTextGender.setEnabled(true);
                editTextEmail.setEnabled(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecord();
                finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord();
                finish();
            }
        });

    }

    private void deleteRecord(){
        if (user.isEmpty() || userFitness.isEmpty()) {
            Toast.makeText(this, "No record to delete!", Toast.LENGTH_SHORT).show();
            return;
        }

        int result2 = userFitnessDao.deleteUserFitness(userFitness.get(index));
        int result1 = usersDao.deleteUsers(user.get(index));

        if (result1 > 0 && result2 > 0) {
            Toast.makeText(this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            user.remove(index);
            userFitness.remove(index);
            finish(); //return to main activity
        } else {
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showRecord(){
        if(index == -1){
            Toast.makeText( this,  "No account registered", Toast.LENGTH_LONG).show();
            finish();
        }else{
            editTextFirstName.setText(user.get(index).getFirstName());
            editTextLastName.setText(user.get(index).getLastName());
            editTextAge.setText(String.valueOf(user.get(index).getAge()));
            editTextGoal.setText(String.valueOf(user.get(index).getFitnessGoal()));
            editTextEmail.setText(user.get(index).getEmail());
            if(user.get(index).isGender()){
                editTextGender.setText("Male");
            }else{
                editTextGender.setText("Female");
            }

        }
    }

    private void updateRecord(){
        SharedPreferences prefs = getSharedPreferences("mySteps",MODE_PRIVATE);
        String fName = editTextFirstName.getText().toString();
        String lName = editTextLastName.getText().toString();
        String ageStr = editTextAge.getText().toString();
        String goalStr = editTextGoal.getText().toString();
        String genderStr = editTextGender.getText().toString();
        String email = editTextEmail.getText().toString();

        int age = 0;
        int goal = 0;
        boolean gender = genderStr.equalsIgnoreCase("male");

        try {
            age = Integer.parseInt(ageStr);
            goal = Integer.parseInt(goalStr);

            //---Update User---
            user.get(index).setAge(age);
            user.get(index).setFitnessGoal(goal);
            user.get(index).setFirstName(fName);
            user.get(index).setLastName(lName);
            user.get(index).setEmail(email);

            //---Update User Fitness---
            userFitness.get(index).setEmail(email);
            userFitness.get(index).setSteps(goal);


            int result1 = usersDao.updateUsers(user.get(index));
            int result2 = userFitnessDao.updateUserFitness(userFitness.get(index));

            if (result1 == 0 || result2 == 0) {
                Toast.makeText(this, "Database update failed!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid number entered for age or goal", Toast.LENGTH_SHORT).show();
            return; // stop update if invalid input
        }


        // Update shared prefs
        prefs.edit()
                .putString("email", email)
                .putString("firstName", fName)
                .putString("lastName", lName)
                .putInt("age", age)
                .putBoolean("gender", gender)
                .putInt("goal", goal) // store as int instead of String
                .apply();

        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
    }


}