package com.wayfare.backend.model;

import com.wayfare.backend.exception.InvalidInputException;
import com.wayfare.backend.validator.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

import static com.wayfare.backend.helper.helper.generateSalt;
import static com.wayfare.backend.helper.helper.hashPassword;


// Regex guide for validation https://www.w3schools.com/java/java_regex.asp
@Document("users")
public class User{

    @Id
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private byte[] secret;
    private byte[] salt;
    private Instant dateCreated;
    private Instant dateModified;

    public User(String username, String email, String firstName, String lastName, String phoneNumber, String secret) throws NoSuchAlgorithmException, InvalidInputException {

        ArrayList<String> errors = new ArrayList<>();
        String usernameInvalid = new AlphanumericValidator(username, "Invalid username").validateRegex();
        String emailInvalid = new EmailValidator(email).validateRegex();
        String firstNameInvalid = new AlphabeticalValidator(firstName, "Invalid first name").validateRegex();
        String lastNameInvalid = new AlphabeticalValidator(lastName, "Invalid last name").validateRegex();
        String phoneNumberInvalid = new PhoneNumberValidator(phoneNumber).validateRegex();
        String passwordInvalid = new PasswordValidator(secret).validateRegex();

        if (usernameInvalid != null)
            errors.add(usernameInvalid);
        if (emailInvalid != null)
            errors.add(emailInvalid);
        if (firstNameInvalid != null)
            errors.add(firstNameInvalid);
        if (lastNameInvalid != null)
            errors.add(lastNameInvalid);
        if (phoneNumberInvalid != null)
            errors.add(phoneNumberInvalid);
        if (passwordInvalid != null)
            errors.add(passwordInvalid);

        if (!errors.isEmpty())
            throw new InvalidInputException(errors);

        this.dateCreated = Instant.now();
        setUsername(username);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setSecret(secret);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
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

    public byte[] getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) throws NoSuchAlgorithmException{
        this.salt = generateSalt();
        this.secret = hashPassword(secret, getSalt());
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