package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "estate")
public class Estate {

    // --- Attribute ---
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private int price;
    private int surface;
    private int number_rooms;
    private String description;
    private String address;
    private int zipCode;
    private String city;
    private double lat;
    private double lng;
    private String points_interest;
    private boolean isSold;
    private Date entry_date;
    private Date date_sale;
    private String agent_name;
    private int number_picture;

    // --- Constructor ---
    public Estate(){

    }

    public Estate(String type, int price, int surface, int number_rooms, String description, String address, int zipCode, String city, double lat, double lng, String points_interest, boolean isSold, Date entry_date, Date date_sale, String agent_name, int number_picture) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.number_rooms = number_rooms;
        this.description = description;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.points_interest = points_interest;
        this.isSold = isSold;
        this.entry_date = entry_date;
        this.date_sale = date_sale;
        this.agent_name = agent_name;
        this.number_picture = number_picture;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public int getPrice() {
        return price;
    }
    public int getSurface() {
        return surface;
    }
    public int getNumber_rooms() {
        return number_rooms;
    }
    public String getDescription() {
        return description;
    }
    public String getAddress() {
        return address;
    }
    public int getZipCode() {
        return zipCode;
    }
    public String getCity() {
        return city;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public String getPoints_interest() {
        return points_interest;
    }
    public boolean isSold() {
        return isSold;
    }
    public Date getEntry_date() {
        return entry_date;
    }
    public Date getDate_sale() {
        return date_sale;
    }
    public String getAgent_name() {
        return agent_name;
    }
    public int getNumber_picture() {
        return number_picture;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setSurface(int surface) {
        this.surface = surface;
    }
    public void setNumber_rooms(int number_rooms) {
        this.number_rooms = number_rooms;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setPoints_interest(String points_interest) {
        this.points_interest = points_interest;
    }
    public void setSold(boolean sold) {
        isSold = sold;
    }
    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
    }
    public void setDate_sale(Date date_sale) {
        this.date_sale = date_sale;
    }
    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }
    public void setNumber_picture(int number_picture) {
        this.number_picture = number_picture;
    }

}
