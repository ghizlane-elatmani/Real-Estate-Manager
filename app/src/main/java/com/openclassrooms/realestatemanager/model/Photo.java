package com.openclassrooms.realestatemanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos", foreignKeys = @ForeignKey(entity = Estate.class,
        parentColumns = "id",
        childColumns = "estate_id"))
public class Photo {

    // --- Attribute ---
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String uri;
    private String label;
    @ColumnInfo(name = "estate_id")
    private int estateId;

    // --- Constructor ---
    public Photo() {
    }

    public Photo(String uri, String label, int estateId) {
        this.uri = uri;
        this.label = label;
        this.estateId = estateId;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }
    public String getUri() {
        return uri;
    }
    public String getLabel() {
        return label;
    }
    public int getEstateId() {
        return estateId;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setEstateId(int estateId) {
        this.estateId = estateId;
    }


}