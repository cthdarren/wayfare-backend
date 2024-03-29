package com.wayfare.backend.model.object;

import com.wayfare.backend.model.BadgeEnum;
import com.wayfare.backend.model.RoleEnum;
import com.wayfare.backend.model.User;

import java.time.Instant;
import java.util.ArrayList;

public class PublicUserData {
    private String id;
    private String pictureUrl;
    private String username;
    private String firstName;
    private String lastName;

    public PublicUserData(){}

    public PublicUserData(String id, String pictureUrl, String username, String firstName, String lastName){
        this.id = id;
        this.pictureUrl = pictureUrl;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public PublicUserData(User user){
        this.id = user.getId();
        this.pictureUrl = user.getPictureUrl();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }


    public String getPictureUrl() {
        return pictureUrl;
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

    public String getId() {
        return id;
    }
}
