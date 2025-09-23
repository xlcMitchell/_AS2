package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

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

    @Insert
    long insertUserFitness(UserFitness userFitness);

    @Update
    int updateUserFitness(UserFitness userFitness);

    @Delete
    void deleteUserFitness(UserFitness userFitness);
}
