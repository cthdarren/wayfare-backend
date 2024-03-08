package com.wayfare.backend.helper;

import java.util.regex.Pattern;

public class NameValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("[A-Za-z]");
    public static String errMsg = "Only alphabetical characters are supported";
    public NameValidator(String input){
        super(pattern, errMsg, input);
    }
}
