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

    LiveData<Estate> getEstate(int id){
        return estateDao.getEstate(id);
    }

    LiveData<List<Estate>> getAllEstates(){
        return estateDao.getAllEstates();
    }

    void insertEstate(Estate estate){
        estateDao.insertEstate(estate);
    }

    void updateEstate(Estate estate){
        estateDao.updateEstate(estate);
    }


}
