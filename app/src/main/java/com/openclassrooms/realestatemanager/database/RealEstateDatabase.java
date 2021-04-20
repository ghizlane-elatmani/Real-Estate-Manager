package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

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
                estateValue1.put("type", "loft");
                estateValue1.put("price", 2575000);
                estateValue1.put("surface", 200);
                estateValue1.put("number_rooms", 4);
                estateValue1.put("description", "Very nice apartment located on the 6th and last floor of 1399sq ft. Located in the heart of town and close to all amenities. It has 3 large bedrooms, 2 bathrooms and a large living room with a terrace overlooking a private park.");
                estateValue1.put("address", "688 Thatcher Dr.Yonkers");
                estateValue1.put("zipCode", 10701);
                estateValue1.put("lat", 40.970340);
                estateValue1.put("lng", -73.878320);
                estateValue1.put("city", "NY");
                estateValue1.put("points_interest", "Transportation, school");
                estateValue1.put("isSold", "Available");
                estateValue1.put("entry_date", "");
                estateValue1.put("date_sale", "");
                estateValue1.put("agent_name", "Oliver Queen");
                estateValue1.put("number_picture", 0);
                db.insert("estate", OnConflictStrategy.IGNORE, estateValue1);

            }
        };
    }
}