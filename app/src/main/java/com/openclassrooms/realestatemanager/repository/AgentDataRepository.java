package com.openclassrooms.realestatemanager.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.AgentDao;
import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

public class AgentDataRepository {

    private final AgentDao agentDao;

    public AgentDataRepository(AgentDao agentDao){
        this.agentDao = agentDao;
    }

    public LiveData<List<Agent>> getAllAgent(){
        return agentDao.getAllAgents();
    }

}
