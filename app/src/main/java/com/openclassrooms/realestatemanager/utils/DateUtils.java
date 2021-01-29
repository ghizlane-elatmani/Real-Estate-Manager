package com.openclassrooms.realestatemanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date convertStringToDate(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = simpleDateFormat.parse(stringDate);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}
