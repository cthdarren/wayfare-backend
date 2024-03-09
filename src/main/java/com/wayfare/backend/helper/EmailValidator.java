package com.wayfare.backend.helper;

import java.util.regex.Pattern;

public class EmailValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    public static String errMsg = "Invalid email address";
    public EmailValidator(String input){
        super(pattern, errMsg, input);
    }
}
