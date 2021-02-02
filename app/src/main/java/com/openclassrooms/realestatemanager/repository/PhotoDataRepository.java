package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

public class PhotoDataRepository {

    private final PhotoDao photoDao;

    public PhotoDataRepository(PhotoDao photoDao){
        this.photoDao = photoDao;
    }


    public LiveData<List<Photo>> getAllPhotos(){
        return photoDao.getAllPhotos();
    }

    public LiveData<List<Photo>> getPhotos(int estate_id){
        return photoDao.getPhotos(estate_id);
    }

    public void insertPhoto(Photo photo){
        photoDao.insertPhoto(photo);
    }

    public void updateEstatePhoto(Photo photo){
        photoDao.updateEstatePhoto(photo);
    }

    public void deleteEstatePhoto(Photo photo){
        photoDao.deleteEstatePhoto(photo);
    }


}
