package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserFitnessViewModel extends AndroidViewModel {
    private UserFitnessRepository repository;
    private LiveData<List<UserFitness>> allUserFitness;

    public UserFitnessViewModel(@NonNull Application application) {
        super(application);
        repository = new UserFitnessRepository(application);
        allUserFitness = repository.getAllUserFitness();
    }

    public LiveData<List<UserFitness>> getAllUserFitness() {
        return allUserFitness;
    }

    public void insertUserFitness(UserFitness userFitness) {
        repository.insert(userFitness);
    }
}
