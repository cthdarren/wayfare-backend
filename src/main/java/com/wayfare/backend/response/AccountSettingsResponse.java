package com.wayfare.backend.response;

import com.wayfare.backend.model.*;

import java.time.Instant;
import java.util.List;

public class AccountSettingsResponse {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final boolean isVerified;
    private final Instant dateCreated;
    private final String pictureUrl;
    private final String aboutMe;
    private final List<BadgeEnum> badges;
    private final RoleEnum role;


    public AccountSettingsResponse(String username, String firstName, String lastName, String email, String phoneNumber, boolean isVerified, Instant dateCreated, String pictureUrl, String aboutMe, List<BadgeEnum> badges, RoleEnum role){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.dateCreated = dateCreated;
        this.pictureUrl = pictureUrl;
        this.aboutMe = aboutMe;
        this.badges = badges;
        this.role = role;
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

    public Instant getDateCreated() {
        return dateCreated;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public List<BadgeEnum> getBadges() {
        return badges;
    }

    public RoleEnum getRole() {
        return role;
    }
}

