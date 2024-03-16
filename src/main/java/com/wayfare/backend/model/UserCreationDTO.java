package com.wayfare.backend.model;

import com.wayfare.backend.validator.AlphanumericValidator;
import com.wayfare.backend.validator.EmailValidator;
import com.wayfare.backend.validator.PhoneNumberValidator;

import java.util.HashSet;
import java.util.Set;

public class UserCreationDTO extends ValidateClass{
    private String username;
    private String plainPassword;
    private String verifyPassword;
    private String email;
    private String phoneNumber;
    public UserCreationDTO(String username, String plainPassword, String verifyPassword, String email, String phoneNumber, RoleEnum role){
        setUsername(username);
        setPlainPassword(plainPassword);
        verifyPassword = verifyPassword;
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

    @Override
    public void validate() {
        Set<String> tempSet = new HashSet<>();
        tempSet.add(new AlphanumericValidator(getUsername(), "Wrong username format").validateRegex());
        tempSet.add(new EmailValidator(getEmail()).validateRegex());
        tempSet.add(new PhoneNumberValidator(getPhoneNumber()).validateRegex());
        tempSet.remove(null);
        setErrors(tempSet);    }
}
