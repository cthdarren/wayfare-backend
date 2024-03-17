package com.wayfare.backend.validator;

import java.util.regex.Pattern;


/*
        password must contain 1 number (0-9)
        password must contain 1 uppercase letters
        password must contain 1 lowercase letters
        password must contain 1 non-alpha numeric number
        password is 8-30 characters with no space
*/


public class PasswordValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,30}$");
    public static String errMsg = "Invalid password format";
    public PasswordValidator(String input){
        super(pattern, errMsg, input);
    }
}
