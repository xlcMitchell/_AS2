package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserRepository {
    private UsersDao userDao;
    private LiveData<List<Users>> allUsers;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.createDatabaseInstance(application);
        userDao = db.usersDao();
        allUsers = userDao.readAllUsersLive();
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }


}