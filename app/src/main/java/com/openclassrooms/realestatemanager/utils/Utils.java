package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * 0.812);
    }

    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros / 0.812);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wifi.isWifiEnabled();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)  context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String formatNumberCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(Double.parseDouble(number));
    }

    public static String getEstatePointInterest(Chip addPointInterestSchoolChip, Chip addPointInterestShopChip, Chip addPointInterestParkChip, Chip addPointInterestTransportationChip) {
        String amenitiesInput = "(";
        if (addPointInterestSchoolChip.isChecked()) {
            amenitiesInput +="'School'";
        }
        if (addPointInterestShopChip.isChecked()) {
            if (!amenitiesInput.equals("("))
                amenitiesInput += ", ";
            amenitiesInput += "'Shop'";
        }
        if (addPointInterestParkChip.isChecked()) {
            if (!amenitiesInput.equals("("))
                amenitiesInput += ", ";
            amenitiesInput += "'Park'";
        }
        if (addPointInterestTransportationChip.isChecked()) {
            if (!amenitiesInput.equals("("))
                amenitiesInput += ", ";
            amenitiesInput += "'Transportation'";
        }
        amenitiesInput += ")";
        return amenitiesInput;
    }
}
