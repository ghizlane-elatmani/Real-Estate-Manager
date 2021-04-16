package com.openclassrooms.realestatemanager.model;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos", foreignKeys = @ForeignKey(entity = Estate.class,
        parentColumns = "id",
        childColumns = "estate_id"))
public class Photo {

    // --- Attribute ---
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Uri uri;
    private String label;
    @ColumnInfo(name = "estate_id")
    private int estateId;

    // --- Constructor ---
    public Photo() {
    }

    @Ignore
    public Photo(Uri uri, String label){
        this.uri  = uri;
        this.label = label;
    }

    public Photo(Uri uri, String label, int estateId) {
        this.uri = uri;
        this.label = label;
        this.estateId = estateId;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }
    public Uri getUri() {
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
    public void setUri(Uri uri) {
        this.uri = uri;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public void setEstateId(int estateId) {
        this.estateId = estateId;
    }


}