package com.wayfare.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.regex.Pattern;

@Document("users")
public class User{

    @Id
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String secret;
    private byte[] salt;
    private Instant dateCreated;
    private Instant dateModified;

    public User(String username, String email, String firstName, String lastName, String phoneNumber, String secret) {
        super();
        this.dateCreated = Instant.now();
        generateSalt();
        setUsername(username);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setSecret(secret);
    }

    public void generateSalt(){
        final Random r = new SecureRandom();
        byte[] salt = new byte[64];
        r.nextBytes(salt);
        this.salt = salt;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String _email) {
        this.email = _email;
    }

//    public boolean validateEmail(String email) {
//        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        return Pattern.compile(regexPattern).matcher(email).matches();
//    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String _phoneNumber) {
        this.phoneNumber = _phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String _firstName) {
        this.firstName = _firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String _lastName) {
        this.lastName = _lastName;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String _secret) {
        this.secret = _secret;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant _dateModified) {
        this.dateModified = _dateModified;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public byte[] getSalt() {
        return salt;
    }
}