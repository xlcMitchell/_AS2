package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class UserFitnessRepository {

    private UserFitnessDao userFitnessDao;
    private LiveData<List<UserFitness>> allUserFitness;

    public UserFitnessRepository(Application application) {
        AppDatabase db = AppDatabase.createDatabaseInstance(application);
        userFitnessDao = db.userFitnessDao();
        allUserFitness = userFitnessDao.readAllUserFitnessLive();
    }

    public LiveData<List<UserFitness>> getAllUserFitness() {
        return allUserFitness;
    }

    public void insert(UserFitness fitness) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userFitnessDao.insertUserFitness(fitness);
        });
    }
}
