package com.wayfare.backend.validator;

import java.util.regex.Pattern;

public class AlphabeticalValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("[A-Za-z]");
    public AlphabeticalValidator(String input, String errMsg){
        super(pattern, errMsg, input);
    }
}
