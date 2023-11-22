package com.example.hikeapp.model;

public class ObservationItem {
    private String uuid;
    private String observation;
    private String date;
    private String time;
    private String comment;

    public ObservationItem(String uuid, String observation, String date, String time, String comment) {
        this.uuid = uuid;
        this.observation = observation;
        this.date = date;
        this.time = time;
        this.comment = comment;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
