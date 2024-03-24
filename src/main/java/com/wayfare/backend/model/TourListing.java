package com.wayfare.backend.model;

import java.time.Instant;

public class TourListing {
    private String id;
    private String username;
    private String address;
    private Instant tourStartDateTime;
    private Instant tourEndDateTime;
    private Instant dateCreated;
    private Instant dateModified;
    private String userId;
    private String tourName;
    private String country;

    public TourListing(String id, String userId, String address, Instant tourStartDateTime, Instant tourEndDateTime, Instant dateCreated, Instant dateModified, String username, String tourName, String country) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.tourEndDateTime = tourEndDateTime;
        this.tourStartDateTime = tourStartDateTime;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.userId = userId;
        this.tourName = tourName;
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Instant getTourStartDateTime() {
        return tourStartDateTime;
    }

    public void setTourStartDateTime(Instant tourStartDateTime) {
        this.tourStartDateTime = tourStartDateTime;
    }

    public Instant getTourEndDateTime() {
        return tourEndDateTime;
    }

    public void setTourEndDateTime(Instant tourEndDateTime) {
        this.tourEndDateTime = tourEndDateTime;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }
}
