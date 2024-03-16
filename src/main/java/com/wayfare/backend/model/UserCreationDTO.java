package com.wayfare.backend.model;

import com.wayfare.backend.exception.FormatException;
import com.wayfare.backend.validator.AlphanumericValidator;
import com.wayfare.backend.validator.EmailValidator;
import com.wayfare.backend.validator.PasswordValidator;
import com.wayfare.backend.validator.PhoneNumberValidator;

import java.util.Objects;

public class UserCreationDTO extends ValidateClass{
    private String username;
    private String plainPassword;
    private String verifyPassword;
    private String email;
    private String phoneNumber;
    public UserCreationDTO(String username, String plainPassword, String verifyPassword, String email, String phoneNumber, RoleEnum role){
        setUsername(username);
        setPlainPassword(plainPassword);
        setVerifyPassword(verifyPassword);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String password) {
        this.plainPassword = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    @Override
    public void validate(){
        if (!Objects.equals(getVerifyPassword(), getPlainPassword())) {
            addErrors("Passwords do not match");
        }
        addErrors(new AlphanumericValidator(getUsername(), "Wrong username format").validateRegex());
        addErrors(new EmailValidator(getEmail()).validateRegex());
        addErrors(new PhoneNumberValidator(getPhoneNumber()).validateRegex());
        addErrors(new PasswordValidator(getPlainPassword()).validateRegex());
        getErrors().remove(null);
    }
}