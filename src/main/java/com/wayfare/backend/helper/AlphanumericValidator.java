package com.wayfare.backend.helper;

import java.util.regex.Pattern;

public class AlphanumericValidator extends Validator{

    public static Pattern pattern = Pattern.compile("[A-Za-z0-9]");
    public static String errMsg = "Invalid username";
    public AlphanumericValidator(String input){
        super(pattern, errMsg, input);
    }
}
