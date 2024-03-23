package com.wayfare.backend.model;

import com.wayfare.backend.security.WayfareUserDetailService;
import com.wayfare.backend.validator.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TourListing {
    private String id;
    private String username;
    private String address;
    private Instant tourStartDateTime;
    private Instant tourEndDateTime;
    private Instant dateCreated;
    private Instant dateModified;
    private String userID;
    private String tourName;
    private String country;

    public TourListing(String id, String userID, String address, Instant tourStartDateTime, Instant tourEndDateTime, Instant dateCreated, Instant dateModified, String username, String tourName, String country) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.tourEndDateTime = tourEndDateTime;
        this.tourStartDateTime = tourStartDateTime;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
