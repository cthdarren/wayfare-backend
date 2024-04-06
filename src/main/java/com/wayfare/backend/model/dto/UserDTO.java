package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.*;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

//only used for the creation of new users
public class UserDTO extends ValidateClass {
    private final String pictureUrl;
    private final String aboutMe;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String verifyPassword;
    private final String email;
    private final String phoneNumber;
    private final List<String> languagesSpoken;
    public UserDTO(String pictureUrl, String aboutMe, String username, String firstName, String lastName, String password, String verifyPassword, String email, String phoneNumber, List<String> languagesSpoken){
        this.pictureUrl = pictureUrl;
        this.aboutMe = aboutMe;
        this.username = username.toLowerCase();
        this.firstName = StringUtils.capitalize(firstName);
        this.lastName = StringUtils.capitalize(lastName);
        this.password = password;
        this.verifyPassword = verifyPassword;
        this.email = email.toLowerCase();
        this.phoneNumber = phoneNumber;
        this.languagesSpoken = languagesSpoken;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
    public String getAboutMe() {
        return aboutMe;
    }
    public String getUsername() {return username;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getPlainPassword() {return password;}
    public String getVerifyPassword() {return verifyPassword;}
    public String getEmail() {return email;}
    public List<String> getLanguagesSpoken() {return languagesSpoken;}

    public String getPhoneNumber() {return phoneNumber;}
    public void validate(){
        if (!Objects.equals(getVerifyPassword(), getPlainPassword())) {
            addErrors("Passwords do not match");
        }
        if (getLanguagesSpoken().size() == 0)
            addErrors("You must be able to speak at least one language");
        addErrors(new UsernameValidator(getUsername()).validateRegex());
        addErrors(new AlphabeticalValidator(getFirstName(), "First name can only accept alphabetical characters").validateRegex());
        addErrors(new AlphabeticalValidator(getLastName(), "Last name can only accept alphabetical characters").validateRegex());
        addErrors(new EmailValidator(getEmail()).validateRegex());
        addErrors(new PhoneNumberValidator(getPhoneNumber()).validateRegex());
        addErrors(new PasswordValidator(getPlainPassword()).validateRegex());
        getErrors().remove(null);
    }


}
