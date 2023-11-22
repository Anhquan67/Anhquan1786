package com.example.hikeapp.model;

public class HikeModel {
    private String uuid;
    private String name;
    private String location;
    private String date;
    private String isParkingAvailable;
    private String length;
    private String level;
    private String description;
    private String image;

    public HikeModel(String uuid, String name, String location, String date, String isParkingAvailable, String length, String level, String description, String image) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.date = date;
        this.isParkingAvailable = isParkingAvailable;
        this.length = length;
        this.level = level;
        this.description = description;
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String isParkingAvailable() {
        return isParkingAvailable;
    }

    public void setIsParkingAvailable(String isParkingAvailable) {
        this.isParkingAvailable = isParkingAvailable;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
