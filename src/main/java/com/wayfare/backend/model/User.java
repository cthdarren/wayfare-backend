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

import static com.wayfare.backend.helper.helper.generateSalt;
import static com.wayfare.backend.helper.helper.hashPassword;


// Regex guide for validation https://www.w3schools.com/java/java_regex.asp


@Document(collection = "users")
public class User{
    private WayfareUserDetailService wayfareUserDetailService;
    private String id;

    private String username;

    private String email;

    private String phoneNumber;

    private String encryptedPassword;

    private RoleEnum role;
    private Boolean isVerified;

    public User() {
    }

    public User(String username, String password, String email, String phoneNumber, RoleEnum role) {
        super();
        setUsername(username);
        setEncryptedPassword(password);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRole(role);
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

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public RoleEnum getRole() {
        return role;
    }

    public String getEmail() {return email; }
    public String getPhoneNumber() {return phoneNumber; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEncryptedPassword(String password) {
        BCryptPasswordEncoder test = new BCryptPasswordEncoder();
        this.encryptedPassword = test.encode(password);
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

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}