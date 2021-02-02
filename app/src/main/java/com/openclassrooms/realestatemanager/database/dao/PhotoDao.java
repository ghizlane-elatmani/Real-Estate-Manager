package com.openclassrooms.realestatemanager.database.dao;

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

    @Query("SELECT * FROM photos")
    LiveData<List<Photo>> getAllPhotos();

    @Query("SELECT * FROM photos WHERE estate_id = :estate_id")
    LiveData<List<Photo>> getPhotos(int estate_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(Photo photo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEstatePhoto(Photo photo);

    @Delete
    void deleteEstatePhoto(Photo photo);

}
