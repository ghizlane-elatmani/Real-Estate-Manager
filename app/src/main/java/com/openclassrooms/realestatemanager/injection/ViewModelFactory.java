package com.openclassrooms.realestatemanager.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.repository.EstateDataRepository;
import com.openclassrooms.realestatemanager.repository.PhotoDataRepository;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final AgentDataRepository agentDataSource;
    private final EstateDataRepository estateDataSource;
    private final PhotoDataRepository photoDataSource;
    private final Executor executor;

    public ViewModelFactory(AgentDataRepository agentDataSource, EstateDataRepository estateDataSource, PhotoDataRepository photoDataSource, Executor executor){
        this.agentDataSource = agentDataSource;
        this.estateDataSource = estateDataSource;
        this.photoDataSource = photoDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EstateViewModel(agentDataSource, estateDataSource, photoDataSource, executor);
    }
}
