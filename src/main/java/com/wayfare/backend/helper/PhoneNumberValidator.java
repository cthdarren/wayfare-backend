package com.wayfare.backend.helper;

import java.util.regex.Pattern;

public class PhoneNumberValidator extends Validator{

    public static Pattern pattern = Pattern.compile("[89]\\d{7}");
    public static String errMsg = "Invalid phone number";
    public PhoneNumberValidator(String input){
        super(pattern, errMsg, input);
    }
}
