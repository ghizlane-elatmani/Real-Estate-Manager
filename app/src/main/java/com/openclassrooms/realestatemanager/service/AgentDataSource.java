package com.openclassrooms.realestatemanager.service;

import com.openclassrooms.realestatemanager.model.Agent;

import java.util.Arrays;
import java.util.List;

public class AgentDataSource {

    public static List<Agent> FAKE_USERS = Arrays.asList(
            new Agent(1, "Jake", "Legrand", "jake@gmail.com"),
            new Agent(2, "Paul", "Marin", "paul@gmail.com"),
            new Agent(3, "Phil", "Park", "phil@gmail.com"),
            new Agent(4, "Guillaume","Michelin", "guillaume@gmail.com"),
            new Agent(5, "Francis","Leneuf", "francis@gmail.com")
    );

}