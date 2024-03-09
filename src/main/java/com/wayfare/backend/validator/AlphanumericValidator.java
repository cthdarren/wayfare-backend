package com.wayfare.backend.validator;

import java.util.regex.Pattern;

public class AlphanumericValidator extends BaseValidator {

    public static Pattern pattern = Pattern.compile("[A-Za-z0-9]");
    public AlphanumericValidator(String input, String errMsg){
        super(pattern, errMsg, input);
    }
}
