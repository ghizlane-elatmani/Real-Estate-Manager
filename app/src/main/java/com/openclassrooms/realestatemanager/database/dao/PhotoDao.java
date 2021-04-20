package com.openclassrooms.realestatemanager.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {

    // --- INSERT ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPhoto(Photo photo);

    @Insert
    void insertAll(Photo... photos);

    // --- DELETE ---
    @Delete
    void deletePhoto(Photo photo);

    // --- QUERY ---
    @Query("SELECT * FROM photos WHERE estate_id = :estate_id")
    LiveData<List<Photo>> getPhotos(long estate_id);

    @Query("SELECT * FROM photos WHERE estate_id = :estate_id LIMIT 1")
    LiveData<Photo> getOnePhoto(long estate_id);

    @Query("SELECT * FROM photos WHERE estate_id = :estate_id")
    Cursor getPicturesWithCursor(long estate_id);

}
