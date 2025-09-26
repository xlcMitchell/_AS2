package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UsersDao {
    @Query("SELECT * FROM users")
      List <Users> readAllUsers();
    // New LiveData method
    @Query("SELECT * FROM Users")
    LiveData<List<Users>> readAllUsersLive();

    @Insert
    long insertUsers(Users users);

    @Update
    int updateUsers(Users users);

    @Delete
    int deleteUsers(Users users);
}
