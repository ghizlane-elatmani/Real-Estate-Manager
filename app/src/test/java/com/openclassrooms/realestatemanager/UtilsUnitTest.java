package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class UtilsUnitTest {

    @Test
    public void convertDollarToEuroWithSuccess() {
        int dollar = 100;
        int euros = (int) Math.round(dollar * 0.812);

        assertEquals(euros, Utils.convertDollarToEuro(dollar));
    }

    @Test
    public void convertEuroToDollarWithSuccess() {
        int euro = 100;
        int dollar = (int) Math.round(euro / 0.812);

        assertEquals(dollar, Utils.convertEuroToDollar(euro));
    }

    @Test
    public void getTodayDateWithSuccess() {
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(dateFormat.format(today), Utils.getTodayDate());
    }

}
