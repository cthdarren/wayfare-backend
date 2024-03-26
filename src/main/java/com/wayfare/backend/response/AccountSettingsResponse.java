package com.wayfare.backend.response;

import com.wayfare.backend.model.BadgeEnum;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;

import java.time.Instant;
import java.util.List;

public class AccountSettingsResponse {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final boolean isVerified;

    public AccountSettingsResponse(String username, String firstName, String lastName, String email, String phoneNumber, boolean isVerified){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }
}

