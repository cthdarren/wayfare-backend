package com.wayfare.backend.model;

import com.wayfare.backend.helper.AlphanumericValidator;
import com.wayfare.backend.helper.EmailValidator;
import com.wayfare.backend.helper.NameValidator;
import com.wayfare.backend.helper.PhoneNumberValidator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;


// Regex guide for validation https://www.w3schools.com/java/java_regex.asp
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
//        try {
            setUsername(username);
            setEmail(email);
            setFirstName(firstName);
            setLastName(lastName);
            setPhoneNumber(phoneNumber);
            setSecret(secret);
//        }
//        catch (IllegalArgumentException e){

//        }
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

    public void setUsername(String username) {
        new AlphanumericValidator(username).validateRegex();
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        new EmailValidator(email).validateRegex();
        this.email = email;
    }



    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        new PhoneNumberValidator(phoneNumber).validateRegex();
        this.phoneNumber = phoneNumber;
    }



    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        new NameValidator(firstName).validateRegex();
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        new NameValidator(firstName).validateRegex();
        this.lastName = lastName;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

    public byte[] getSalt() {
        return salt;
    }
}