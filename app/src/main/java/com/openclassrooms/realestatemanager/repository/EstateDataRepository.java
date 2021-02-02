package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.EstateDao;
import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

public class EstateDataRepository {

    private final EstateDao estateDao;

    public EstateDataRepository(EstateDao estateDao){
        this.estateDao = estateDao;
    }

    public LiveData<Estate> getEstate(int id){
        return estateDao.getEstate(id);
    }

    public LiveData<List<Estate>> getAllEstates(){
        return estateDao.getAllEstates();
    }

    public void insertEstate(Estate estate){
        estateDao.insertEstate(estate);
    }

    public void updateEstate(Estate estate){
        estateDao.updateEstate(estate);
    }


}
