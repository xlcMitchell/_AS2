package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<Users>> allUsers;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllUsers();
    }

    public LiveData<List<Users>> getAllUsers() {
        return allUsers;
    }

    public void insertUser(Users user){
        repository.insert(user);
    }




}