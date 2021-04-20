package com.openclassrooms.realestatemanager.viewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.repository.EstateDataRepository;
import com.openclassrooms.realestatemanager.repository.PhotoDataRepository;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EstateViewModel extends ViewModel {

    // REPOSITORIES
    private final AgentDataRepository agentDataSource;
    private final EstateDataRepository estateDataSource;
    private final PhotoDataRepository photoDataSource;
    private final Executor executor;
    private ExecutorService executorService;

    private MutableLiveData<List<Estate>> estatesList = new MutableLiveData<>();
    private MutableLiveData<Long> selectedEstateId = new MutableLiveData<>();
    private MutableLiveData<List<Uri>> uriList = new MutableLiveData<>();

    public EstateViewModel(AgentDataRepository agentDataSource, EstateDataRepository estateDataSource, PhotoDataRepository photoDataSource, Executor executor) {
        this.agentDataSource = agentDataSource;
        this.estateDataSource = estateDataSource;
        this.photoDataSource = photoDataSource;
        this.executor = executor;
    }

    // --- Agent ---
    public LiveData<List<Agent>> getAllAgent(){
        return agentDataSource.getAllAgent();
    }

    // --- Estate ---
    public LiveData<List<Estate>> getAllEstate() {
        return estateDataSource.getAllEstate();
    }

    public LiveData<Estate> getEstate(long estateId) {
        return estateDataSource.getEstate(estateId);
    }

    public LiveData<List<Estate>> getEstateByCityAndCountry(String city) {
        return estateDataSource.getEstateByCityAndCountry(city);
    }

    public LiveData<List<Estate>> getAllEstatesAccordingToUserSearch(SupportSQLiteQuery query) {
        return estateDataSource.getAllEstatesAccordingToUserSearch(query);
    }

    public long createEstate(final Estate estate) {
        Callable<Long> insertCallable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return estateDataSource.createEstate(estate);
            }
        };
        long row_id = 0;

        executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            row_id = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return row_id;
    }

    public int updateEstate(final Estate estate) {
        Callable<Integer> insertCallable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return estateDataSource.updateEstate(estate);
            }
        };
        int nbRows = 0;

        executorService = Executors.newFixedThreadPool(1);
        Future<Integer> future = executorService.submit(insertCallable);
        try {
            nbRows = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return nbRows;
    }

    public void addEstateList(List<Estate> properties) {
        estatesList.setValue(properties);
    }

    public LiveData<List<Estate>> getEstateList() {
        return estatesList;
    }

    public void addSelectedEstateId(long estateId) {
        selectedEstateId.setValue(estateId);
    }

    public LiveData<Long> getSelectedEstateId() {
        return selectedEstateId;
    }

    // --- Photo --
    public long createPictures(final Photo photo) {
        Callable<Long> insertCallable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return photoDataSource.insertPhoto(photo);
            }
        };
        long rowId = 0;

        executorService = Executors.newFixedThreadPool(1);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public LiveData<List<Photo>> getPhotos(long estateId) {
        return photoDataSource.getPhotos(estateId);
    }

    public LiveData<Photo> getOnePicture(long estateId) {
        return photoDataSource.getOnePhoto(estateId);
    }

    public void deletePhoto(final Photo photo) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                photoDataSource.deletePicture(photo);
            }
        });
    }

    public void addUriList(List<Uri> uriList) {
        this.uriList.setValue(uriList);
    }

    public LiveData<List<Uri>> getUriList() {
        return uriList;
    }

}
