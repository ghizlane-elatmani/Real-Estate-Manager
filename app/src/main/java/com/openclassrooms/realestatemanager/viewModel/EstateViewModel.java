package com.openclassrooms.realestatemanager.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.repository.EstateDataRepository;
import com.openclassrooms.realestatemanager.repository.PhotoDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class EstateViewModel extends ViewModel {

    // REPOSITORIES
    private final AgentDataRepository agentDataSource;
    private final EstateDataRepository estateDataSource;
    private final PhotoDataRepository photoDataSource;
    private final Executor executor;

    // DATA


    public EstateViewModel(AgentDataRepository agentDataSource, EstateDataRepository estateDataSource, PhotoDataRepository photoDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.estateDataSource = estateDataSource;
        this.photoDataSource = photoDataSource;
        this.executor = executor;
    }


    // ------------
    // FOR AGENT
    // ------------
    public LiveData<List<Agent>> getAllAgent(){
        return agentDataSource.getAllAgent();
    }


    // ------------
    // FOR ESTATE
    // ------------
    public LiveData<Estate> getEstate(int id){
        return estateDataSource.getEstate(id);
    }

    public LiveData<List<Estate>> getAllEstates(){
        return estateDataSource.getAllEstates();
    }

    public void insertEstate(Estate estate){
        estateDataSource.insertEstate(estate);
    }

    public void updateEstate(Estate estate){
        estateDataSource.updateEstate(estate);
    }


    // ------------
    // FOR PHOTO
    // ------------
    public LiveData<List<Photo>> getAllPhotos(){
        return photoDataSource.getAllPhotos();
    }

    public LiveData<List<Photo>> getPhotos(int estate_id){
        return photoDataSource.getPhotos(estate_id);
    }

    public void insertPhoto(Photo photo){
        photoDataSource.insertPhoto(photo);
    }

    public void updateEstatePhoto(Photo photo){
        photoDataSource.updateEstatePhoto(photo);
    }

    public void deleteEstatePhoto(Photo photo){
        photoDataSource.deleteEstatePhoto(photo);
    }


}
