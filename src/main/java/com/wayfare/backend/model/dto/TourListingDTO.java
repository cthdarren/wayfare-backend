package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.*;

import java.time.Instant;
import java.util.Objects;


public class TourListingDTO extends ValidateClass {
    private String id;
    private String username;
    private String address;
    private Instant tourStartDateTime;
    private Instant tourEndDateTime;

    public TourListingDTO(String id, String username, String address, Instant tourStartDateTime, Instant tourEndDateTime) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.tourStartDateTime = tourStartDateTime;
        this.tourEndDateTime = tourEndDateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public void validate() {
        // TODO Validate that user exists

        if (this.tourEndDateTime.isBefore(tourStartDateTime)) {
            addErrors("End of tour cannot be before start of tour");
        }
    }
}
