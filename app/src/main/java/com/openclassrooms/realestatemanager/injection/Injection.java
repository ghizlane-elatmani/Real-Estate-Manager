package com.openclassrooms.realestatemanager.injection;

import android.content.Context;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.repository.AgentDataRepository;
import com.openclassrooms.realestatemanager.repository.EstateDataRepository;
import com.openclassrooms.realestatemanager.repository.PhotoDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    public static AgentDataRepository provideAgentDataSource(Context context){
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new AgentDataRepository(database.agentDao());
    }

    public static EstateDataRepository provideEstateDataSource(Context context){
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new EstateDataRepository(database.estateDao());
    }

    public static PhotoDataRepository providePhotoDataSource(Context context){
        RealEstateDatabase database = RealEstateDatabase.getInstance(context);
        return new PhotoDataRepository(database.photoDao());
    }

    public static Executor provideExecutor(){
        return Executors.newSingleThreadExecutor();
    }

    public static ViewModelFactory provideViewModelFactory(Context context){
        AgentDataRepository dataSourceAgent = provideAgentDataSource(context);
        EstateDataRepository dataSourceEstate = provideEstateDataSource(context);
        PhotoDataRepository dataSourcePhoto = providePhotoDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceAgent, dataSourceEstate, dataSourcePhoto, executor);
    }

}
