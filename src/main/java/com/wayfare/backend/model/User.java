package com.wayfare.backend.model;

import com.wayfare.backend.exception.InvalidInputException;
import com.wayfare.backend.validator.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.wayfare.backend.helper.helper.generateSalt;
import static com.wayfare.backend.helper.helper.hashPassword;


// Regex guide for validation https://www.w3schools.com/java/java_regex.asp
@Document("users")
public class User{

    @Id
    private String id;

    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be empty")
    private String secret;
    private Instant dateCreated;
    private Instant dateModified;
    @DBRef
    private Set<Role> roles = new HashSet<>();

    public User(){
    }

    public User(String username, String email, String fn, String ln, String pn, String secret)
    {
        this.username = username;
        this.email = email;
        this.firstName = fn;
        this.lastName = ln;
        this.phoneNumber = pn;
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}