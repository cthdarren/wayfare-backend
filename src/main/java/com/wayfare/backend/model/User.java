package com.wayfare.backend.model;

import com.wayfare.backend.validator.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


// Regex guide for validation https://www.w3schools.com/java/java_regex.asp


@Document(collection = "users")
public class User{
    private String id;

    private String pictureUrl;
    private String aboutMe;
    private ArrayList<BadgeEnum> badges;
    private String username;

    private String firstName;
    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private List<String> languagesSpoken;

    private RoleEnum role;
    private Boolean isVerified;
    private Instant dateModified;
    private Instant dateCreated;

    public User(String pictureUrl,
                String aboutMe,
                ArrayList<BadgeEnum> badges,
                String username,
                String firstName,
                String lastName,
                String password,
                String email,
                String phoneNumber,
                RoleEnum role,
                Boolean isVerified,
                Instant dateModified,
                Instant dateCreated,
                List<String> languagesSpoken
    ) {
        super();
        setPictureUrl(pictureUrl);
        setAboutMe(aboutMe);
        setBadges(badges);
        setUsername(username);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRole(role);
        setIsVerified(isVerified);
        setDateModified(dateModified);
        setLanguagesSpoken(languagesSpoken);
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public ArrayList<BadgeEnum> getBadges() {
        return badges;
    }

    public void setBadges(ArrayList<BadgeEnum> badges) {
        this.badges = badges;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public String getEmail() {return email; }
    public String getPhoneNumber() {return phoneNumber; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public void setLanguagesSpoken(List<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }


}