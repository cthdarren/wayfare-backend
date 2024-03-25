package com.wayfare.backend.request;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.AlphabeticalValidator;
import com.wayfare.backend.validator.PhoneNumberValidator;
import com.wayfare.backend.validator.UsernameValidator;

public class EditAccountDetailsRequest extends ValidateClass{
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;

    public EditAccountDetailsRequest(String username, String firstName, String lastName, String phoneNumber){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public void validate() {
        addErrors(new UsernameValidator(username).validateRegex());
        addErrors(new AlphabeticalValidator(firstName, "Invalid first name").validateRegex());
        addErrors(new AlphabeticalValidator(lastName, "Invalid last name").validateRegex());
        addErrors(new PhoneNumberValidator(phoneNumber).validateRegex());
        getErrors().remove(null);
    }
}
