package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.utils.DateUtils;

import java.util.Date;

@Database(entities = {Agent.class, Estate.class, Photo.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverters.class})
public abstract class RealEstateDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile RealEstateDatabase INSTANCE;

    // --- DAO ---
    public abstract AgentDao agentDao();
    public abstract EstateDao estateDao();
    public abstract PhotoDao photoDao();

    // --- ATTRIBUTE ---
    private static Context context;

    // --- INSTANCE ---
    public static RealEstateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RealEstateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RealEstateDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ---

    private static Callback prepopulateDatabase(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("id", 1);
                contentValues1.put("first_name", "Théa");
                contentValues1.put("last_name", "Queen");
                contentValues1.put("mail", "thea_queen@gmail.com");
                db.insert("agent", OnConflictStrategy.IGNORE, contentValues1);

                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("id", 2);
                contentValues2.put("first_name", "Oliver");
                contentValues2.put("last_name", "Queen");
                contentValues2.put("mail", "oliver_queen@gmail.com");
                db.insert("agent", OnConflictStrategy.IGNORE, contentValues2);

                ContentValues estateValue1 = new ContentValues();
                estateValue1.put("id", 1);
                estateValue1.put("type", "penthouse");
                estateValue1.put("price", 450000);
                estateValue1.put("surface", 200);
                estateValue1.put("number_rooms", 4);
                estateValue1.put("description", "House in New York with garage. Close to all amenities. It has 3 large bedrooms, 2 bathrooms and a large living room.");
                estateValue1.put("address", "7409 West Sussex Lane Bay Shore");
                estateValue1.put("zipCode", 11706);
                estateValue1.put("lat", 40.711100);
                estateValue1.put("lng", -73.253030);
                estateValue1.put("city", "NY");
                estateValue1.put("points_interest", "Transportation, school");
                estateValue1.put("isSold", true);
                estateValue1.put("entry_date", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("03-02-2017")));
                estateValue1.put("date_sale", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("18-09-2017")));
                estateValue1.put("agent_name", "Oliver Queen");
                estateValue1.put("number_picture", 6);
                db.insert("estate", OnConflictStrategy.IGNORE, estateValue1);

                ContentValues estateValue2 = new ContentValues();
                estateValue2.put("id", 2);
                estateValue2.put("type", "penthouse");
                estateValue2.put("price", 2575000);
                estateValue2.put("surface", 200);
                estateValue2.put("number_rooms", 4);
                estateValue2.put("description", "Very nice apartment located on the 6th and last floor of 1399sq ft. Located in the heart of town and close to all amenities. It has 3 large bedrooms, 2 bathrooms and a large living room with a terrace overlooking a private park.");
                estateValue2.put("address", "688 Thatcher Dr.Yonkers");
                estateValue2.put("zipCode", 10701);
                estateValue2.put("lat", 40.970340);
                estateValue2.put("lng", -73.878320);
                estateValue2.put("city", "NY");
                estateValue2.put("points_interest", "Transportation, school");
                estateValue2.put("isSold", true);
                estateValue2.put("entry_date", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("11-02-2021")));
                estateValue2.put("date_sale", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("15-05-2021")));
                estateValue2.put("agent_name", "Oliver Queen");
                estateValue2.put("number_picture", 4);
                db.insert("estate", OnConflictStrategy.IGNORE, estateValue2);

                ContentValues estateValue3 = new ContentValues();
                estateValue3.put("id", 3);
                estateValue3.put("type", "loft");
                estateValue3.put("price", 2575000);
                estateValue3.put("surface", 200);
                estateValue3.put("number_rooms", 4);
                estateValue3.put("description", "Beautiful house, located in the heart of town and close to all amenities.");
                estateValue3.put("address", "9516 Southampton Street New York");
                estateValue3.put("zipCode", 10031);
                estateValue3.put("lat", 40.893320);
                estateValue3.put("lng", -72.477300);
                estateValue3.put("city", "NY");
                estateValue3.put("points_interest", "Transportation, school, park");
                estateValue3.put("isSold", true);
                estateValue3.put("entry_date", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("12-05-2019")));
                estateValue3.put("date_sale", DataConverters.dateToTimestamp(DateUtils.convertStringToDate("02-09-2020")));
                estateValue3.put("agent_name", "Oliver Queen");
                estateValue3.put("number_picture", 4);
                db.insert("estate", OnConflictStrategy.IGNORE, estateValue3);

                // house n°1
                ContentValues photoValue1 = new ContentValues();
                photoValue1.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house")));
                photoValue1.put("label","Facade");
                photoValue1.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue1);

                ContentValues photoValue2 = new ContentValues();
                photoValue2.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_g")));
                photoValue2.put("label","Garage");
                photoValue2.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue2);

                ContentValues photoValue3 = new ContentValues();
                photoValue3.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_i")));
                photoValue3.put("label","Interior");
                photoValue3.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue3);

                ContentValues photoValue4 = new ContentValues();
                photoValue4.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_bed")));
                photoValue4.put("label","Bedroom");
                photoValue4.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue4);

                ContentValues photoValue5 = new ContentValues();
                photoValue5.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_b1")));
                photoValue5.put("label","Bathroom");
                photoValue5.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue5);

                ContentValues photoValue6 = new ContentValues();
                photoValue6.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_b2")));
                photoValue6.put("label","Bathroom");
                photoValue6.put("estate_id", 1);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue6);

                // house n°2
                ContentValues photoValue7 = new ContentValues();
                photoValue7.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house2")));
                photoValue7.put("label","Facade");
                photoValue7.put("estate_id", 2);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue7);

                ContentValues photoValue8 = new ContentValues();
                photoValue8.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house2_i")));
                photoValue8.put("label","Facade");
                photoValue8.put("estate_id", 2);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue8);

                ContentValues photoValue9 = new ContentValues();
                photoValue9.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house2_b")));
                photoValue9.put("label","Facade");
                photoValue9.put("estate_id", 2);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue9);

                ContentValues photoValue10 = new ContentValues();
                photoValue10.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house2_k")));
                photoValue10.put("label","Facade");
                photoValue10.put("estate_id", 2);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue10);

                // house n°3
                ContentValues photoValue11 = new ContentValues();
                photoValue11.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house3")));
                photoValue11.put("label","Facade");
                photoValue11.put("estate_id", 3);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue11);

                ContentValues photoValue12 = new ContentValues();
                photoValue12.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house3_bed")));
                photoValue12.put("label","Bedroom");
                photoValue12.put("estate_id", 3);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue12);

                ContentValues photoValue13 = new ContentValues();
                photoValue13.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house3_o")));
                photoValue13.put("label","Office");
                photoValue13.put("estate_id", 3);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue13);

                ContentValues photoValue14 = new ContentValues();
                photoValue14.put("uri", String.valueOf(Uri.parse("android.resource://com.openclassrooms.realestatemanager/drawable/house_b")));
                photoValue14.put("label","Bathroom");
                photoValue14.put("estate_id", 3);
                db.insert("photos", OnConflictStrategy.IGNORE, photoValue14);

            }
        };
    }
}