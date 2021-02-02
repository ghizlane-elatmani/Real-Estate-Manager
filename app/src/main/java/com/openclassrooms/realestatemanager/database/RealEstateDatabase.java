package com.openclassrooms.realestatemanager.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.database.dao.EstateDao;
import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;

@Database(entities = {Agent.class, Estate.class, Photo.class}, version = 1)
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

                Agent[] agents = DataSource.getAllAgent();
                for (Agent agent : agents) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", agent.getId());
                    contentValues.put("first_name", agent.getFirst_name());
                    contentValues.put("last_name", agent.getLast_name());
                    contentValues.put("mail", agent.getMail());
                    db.insert("agent", OnConflictStrategy.IGNORE, contentValues);
                }

                Estate[] estates = DataSource.getAllEstate();
                for (Estate estate : estates) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("type", estate.getType());
                    contentValues.put("price", estate.getPrice());
                    contentValues.put("address", estate.getAddress());
                    contentValues.put("surface", estate.getSurface());
                    contentValues.put("number_room", estate.getNumber_rooms());
                    contentValues.put("description", estate.getDescription());
                    contentValues.put("url_picture", estate.getUrl_picture());
                    contentValues.put("points_interest", estate.getPoints_interest());
                    contentValues.put("status", estate.getStatus());
                    contentValues.put("entry_date", String.valueOf(estate.getEntry_date()));
                    contentValues.put("date_sale", String.valueOf(estate.getDate_sale()));
                    contentValues.put("agent_id", estate.getAgent_id());
                    db.insert("estate", OnConflictStrategy.IGNORE, contentValues);
                }

            }
        };
    }
}