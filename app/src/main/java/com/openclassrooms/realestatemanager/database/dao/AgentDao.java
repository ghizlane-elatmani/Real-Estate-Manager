package com.openclassrooms.realestatemanager.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassrooms.realestatemanager.model.Agent;

import java.util.List;

@Dao
public interface AgentDao {

    @Insert
    void insert(Agent agent);

    @Query("SELECT * FROM agent WHERE id = :id")
    LiveData<Agent> getAgent(int id);

    @Query("SELECT * FROM agent")
    LiveData<List<Agent>> getAllAgents();

}
