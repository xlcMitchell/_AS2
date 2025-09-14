package com.example.bit603_mitchell_travis_5080526_as2.DataModel;

import androidx.room.TypeConverter;

import java.util.Date;

//class used to convert data for the date

public class DataTypeConverter {
    @TypeConverter
    public static Date fromTimeStamp(Long value){
        return value == null? null : new Date(value); //convert long to date
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date){
        return date == null?null: date.getTime(); //convert date to long
    }

}