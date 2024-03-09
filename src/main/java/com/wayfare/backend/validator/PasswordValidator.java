package com.wayfare.backend.validator;

import java.util.regex.Pattern;

public class PasswordValidator extends BaseValidator {
    public static Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
    public static String errMsg = "Invalid password format";
    public PasswordValidator(String input){
        super(pattern, errMsg, input);
    }
}
