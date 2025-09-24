package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {Users.class,UserFitness.class},
        version = 4
)
@TypeConverters(DataTypeConverter.class)


public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database = null;

    public abstract UsersDao usersDao();
    public abstract UserFitnessDao userFitnessDao();


    public static AppDatabase createDatabaseInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "database"
            ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
}