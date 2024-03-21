package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.*;

import java.util.Objects;

import static com.wayfare.backend.model.RoleEnum.ROLE_USER;

//only used for the creation of new users
public class UserDTO extends ValidateClass {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String verifyPassword;
    private final String email;
    private final String phoneNumber;
    public UserDTO(String username, String firstName, String lastName, String password, String verifyPassword, String email, String phoneNumber){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.verifyPassword = verifyPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {return username;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getPlainPassword() {return password;}
    public String getVerifyPassword() {return verifyPassword;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public void validate(){
        if (!Objects.equals(getVerifyPassword(), getPlainPassword())) {
            addErrors("Passwords do not match");
        }
        addErrors(new AlphanumericValidator(getUsername(), "Wrong username format").validateRegex());
        addErrors(new AlphabeticalValidator(getFirstName(), "First name can only accept alphabetical characters").validateRegex());
        addErrors(new AlphabeticalValidator(getLastName(), "Last name can only accept alphabetical characters").validateRegex());
        addErrors(new EmailValidator(getEmail()).validateRegex());
        addErrors(new PhoneNumberValidator(getPhoneNumber()).validateRegex());
        addErrors(new PasswordValidator(getPlainPassword()).validateRegex());
        getErrors().remove(null);
    }



}
