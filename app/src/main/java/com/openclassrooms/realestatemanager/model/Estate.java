package com.openclassrooms.realestatemanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "estate", foreignKeys = @ForeignKey(entity = Agent.class,
        parentColumns = "id",
        childColumns = "agent_id"))
public class Estate {

    // --- Attribute ---
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private int price;
    private String address;
    private int surface;
    private int number_rooms;
    private String description;
    private String url_picture;
    private String points_interest;
    private String status;
    private Date entry_date;
    private Date date_sale;
    private int agent_id;

    // --- Constructor ---
    public Estate(){

    }

    public Estate(String type, int price, String address, int surface, int number_rooms, String description, String url_picture, String points_interest, String status, Date entry_date, Date date_sale, int agent_id) {
        this.type = type;
        this.price = price;
        this.address = address;
        this.surface = surface;
        this.number_rooms = number_rooms;
        this.description = description;
        this.url_picture = url_picture;
        this.points_interest = points_interest;
        this.status = status;
        this.entry_date = entry_date;
        this.date_sale = null;
        this.agent_id = agent_id;
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
    public String getAddress() {
        return address;
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
    public String getUrl_picture() {
        return url_picture;
    }
    public String getPoints_interest() {
        return points_interest;
    }
    public String getStatus() {
        return status;
    }
    public Date getEntry_date() {
        return entry_date;
    }
    public Date getDate_sale() {
        return date_sale;
    }
    public int getAgent_id() {
        return agent_id;
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
    public void setAddress(String address) {
        this.address = address;
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
    public void setUrl_picture(String url_picture) {
        this.url_picture = url_picture;
    }
    public void setPoints_interest(String points_interest) {
        this.points_interest = points_interest;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setEntry_date(Date entry_date) {
        this.entry_date = entry_date;
    }
    public void setDate_sale(Date date_sale) {
        this.date_sale = date_sale;
    }
    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }


}
