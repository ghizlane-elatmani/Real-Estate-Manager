package com.openclassrooms.realestatemanager.model;

public class Agent {

    // --- Attribute ---
    private int id;
    private String first_name;
    private String last_name;
    private String mail;


    // --- Constructor ---
    public Agent(){

    }

    public Agent(int id, String first_name, String last_name, String mail) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail = mail;
    }


    // --- Getters ---
    public int getId() {
        return id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getMail() {
        return mail;
    }


    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }


}
