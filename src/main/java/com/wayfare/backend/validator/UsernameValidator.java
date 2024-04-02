package com.wayfare.backend.validator;

import java.util.regex.Pattern;

public class UsernameValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("^[a-z0-9_-]{3,15}$");
    public UsernameValidator(String input){
        super(pattern, "Username must be between 3-15 characters and cannot contain special characters", input);
    }
}
