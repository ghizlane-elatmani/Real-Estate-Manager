package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.database.dao.EstateDao;
import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

public class EstateDataRepository {

    private final EstateDao estateDao;

    public EstateDataRepository(EstateDao estateDao){
        this.estateDao = estateDao;
    }

    // --- GET ---
    public LiveData<Estate> getEstate(long estateId){
        return estateDao.getEstate(estateId);
    }

    public LiveData<List<Estate>> getAllEstate(){
        return estateDao.getAllEstate();
    }

    public LiveData<List<Estate>> getEstateByCityAndCountry(String city){
        return estateDao.getEstateByCityAndCountry(city);
    }

    public LiveData<List<Estate>> getAllEstatesAccordingToUserSearch(SupportSQLiteQuery query){
        return estateDao.getAllEstatesAccordingToUserSearch(query);
    }

    // --- CREATE ---
    public long createEstate(Estate estate){
        return estateDao.insertEstate(estate);
    }

    // --- UPDATE ---
    public int updateEstate(Estate estate){
        return estateDao.updateEstate(estate);
    }

}
