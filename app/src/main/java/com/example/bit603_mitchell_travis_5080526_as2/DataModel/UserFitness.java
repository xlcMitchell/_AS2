package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "user_fitness",foreignKeys={
        @ForeignKey(entity = Users.class,
                parentColumns = "email",
                childColumns = "email")})
public class UserFitness {
    @PrimaryKey(autoGenerate = true)
    private Date date;
    private int email;
    private int steps;

    public UserFitness(Date date, int email, int steps) {
        this.date = date;
        this.email = email;
        this.steps = steps;
    }

    @Override
    public String toString() {
        return "UserFitness{" +
                "date=" + date +
                ", email=" + email +
                ", steps=" + steps +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public int getEmail() {
        return email;
    }

    public int getSteps() {
        return steps;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEmail(int email) {
        this.email = email;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
}
