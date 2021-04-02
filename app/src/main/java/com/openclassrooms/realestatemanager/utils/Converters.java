package com.openclassrooms.realestatemanager.utils;

import android.net.Uri;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Uri fromString(String value){
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String fromUri(Uri value){
        return value.toString();
    }

}

