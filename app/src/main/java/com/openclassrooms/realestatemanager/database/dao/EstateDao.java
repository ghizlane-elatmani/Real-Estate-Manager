package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

@Dao
public interface EstateDao {

    // --- INSERT ---
    @Insert
    long insertEstate(Estate estate);

    @Insert
    void insertAll(Estate... estates);

    // --- UPDATE ---
    @Update
    int updateEstate(Estate estate);

    // --- QUERY ---
    @Query("SELECT * FROM estate")
    LiveData<List<Estate>> getAllEstate();

    @Query("SELECT * FROM estate WHERE id = :id")
    LiveData<Estate> getEstate(long id);

    @Query("SELECT * FROM estate WHERE city = :city")
    LiveData<List<Estate>> getEstateByCityAndCountry(String city);

    @RawQuery(observedEntities = {Estate.class, Photo.class})
    LiveData<List<Estate>> getAllEstatesAccordingToUserSearch(SupportSQLiteQuery query);

    @Query("SELECT * FROM estate WHERE id = :id")
    Cursor getEstateWithCursor(int id);

    @Query("SELECT * FROM estate")
    Cursor getEstateWithCursor();

}
