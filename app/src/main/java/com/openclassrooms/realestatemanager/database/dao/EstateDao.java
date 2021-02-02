package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

@Dao
public interface EstateDao {

    @Query("SELECT * FROM estate WHERE id = :id")
    LiveData<Estate> getEstate(int id);

    @Query("SELECT * FROM estate")
    LiveData<List<Estate>> getAllEstates();

    @Insert
    void insertEstate(Estate estate);

    @Update
    void updateEstate(Estate estate);

}
