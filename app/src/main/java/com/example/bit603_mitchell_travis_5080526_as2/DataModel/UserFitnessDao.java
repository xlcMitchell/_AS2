package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserFitnessDao {
    @Query("SELECT * FROM user_fitness")
    List<UserFitness> readAllUserFitness();

    @Query("SELECT * FROM user_fitness")
    LiveData<List<UserFitness>> readAllUserFitnessLive();

    @Insert
    long insertUserFitness(UserFitness userFitness);

    @Update
    int updateUserFitness(UserFitness userFitness);

    @Delete
    int deleteUserFitness(UserFitness userFitness);
}
