package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Users {
    @PrimaryKey(autoGenerate= true)
    private String email;
    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    private int age;
    private boolean gender;
    @ColumnInfo(name = "fitness_goal")
    private int fitnessGoal;

    public Users(String email, String firstName, String lastName, int age, boolean gender, int fitnessGoal) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.fitnessGoal = fitnessGoal;
    }

    @Override
    public String toString() {
        return "Users{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", fitnessGoal=" + fitnessGoal +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public boolean isGender() {
        return gender;
    }

    public int getFitnessGoal() {
        return fitnessGoal;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setFitnessGoal(int fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }
}
