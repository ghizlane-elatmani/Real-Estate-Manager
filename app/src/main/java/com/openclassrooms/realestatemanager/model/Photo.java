package com.openclassrooms.realestatemanager.model;

public class Photo {

    // --- Attribute ---
    private int id;
    private String url_picture;
    private int estate_id;


    // --- Constructor
    public Photo(){

    }

    public Photo(int id, String url_picture, int estate_id) {
        this.id = id;
        this.url_picture = url_picture;
        this.estate_id = estate_id;
    }


    // --- Getters ---
    public int getId() {
        return id;
    }
    public String getUrl_picture() {
        return url_picture;
    }
    public int getEstate_id() {
        return estate_id;
    }


    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }
    public void setUrl_picture(String url_picture) {
        this.url_picture = url_picture;
    }
    public void setEstate_id(int estate_id) {
        this.estate_id = estate_id;
    }

}
