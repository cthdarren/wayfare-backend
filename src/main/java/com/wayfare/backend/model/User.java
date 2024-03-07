package com.wayfare.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Random;
import java.util.regex.Pattern;

@Document("User")
public class User{

    @Id
    private String _username;
    private String _email;
    private String _firstName;
    private String _lastName;
    private String _phoneNumber;
    private String _secret;
    private byte[] _salt;
    private final Instant _dateCreated;
    private Instant _dateModified;

    public User(String username, String email, String firstName, String lastName, String phoneNumber, String secret) {
        super();
        _dateCreated = Instant.now();
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
        _salt = salt;
    }


    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

//    public boolean validateEmail(String email) {
//        String regexPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        return Pattern.compile(regexPattern).matcher(email).matches();
//    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String _firstName) {
        this._firstName = _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String getSecret() {
        return _secret;
    }

    public void setSecret(String _secret) {
        this._secret = _secret;
    }

    public Instant getDateModified() {
        return _dateModified;
    }

    public void setDateModified(Instant _dateModified) {
        this._dateModified = _dateModified;
    }

    public Instant getDateCreated() {
        return _dateCreated;
    }

    public byte[] getSalt() {
        return _salt;
    }
}