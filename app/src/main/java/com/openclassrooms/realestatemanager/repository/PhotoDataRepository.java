package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

public class PhotoDataRepository {

    private PhotoDao photoDao;

    public PhotoDataRepository(PhotoDao photoDao){
        this.photoDao = photoDao;
    }

    // --- GET ---
    public LiveData<List<Photo>> getPhotos(long estateId){
        return photoDao.getPhotos(estateId);
    }

    public LiveData<Photo> getOnePhoto(long estateId){
        return photoDao.getOnePhoto(estateId);
    }

    // --- CREATE ---
    public long insertPhoto(Photo photo){
        return photoDao.insertPhoto(photo);
    }

    // --- DELETE ---
    public void deletePicture(Photo photo){
        photoDao.deletePhoto(photo);
    }

}

