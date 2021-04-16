package com.openclassrooms.realestatemanager.utils;

import android.content.Context;

import com.openclassrooms.realestatemanager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date convertStringToDate(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date;
        try {
            date = simpleDateFormat.parse(stringDate);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String convertDateToString(Date date, Context context) {
        if (date == null)
            return context.getString(R.string.unknown_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);

    }

}
