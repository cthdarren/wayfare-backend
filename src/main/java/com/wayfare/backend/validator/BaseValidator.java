package com.wayfare.backend.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseValidator {
    public Pattern pattern;
    public String errMsg;
    public String input;
    public BaseValidator(Pattern pattern, String errMsg, String input){
        this.pattern = pattern;
        this.errMsg = errMsg;
        this.input = input;
    }
    public String validateRegex(){
        Matcher matcher = this.pattern.matcher(this.input);
        if (!matcher.find()) {
            return errMsg;
        }
        return null;
    }
}
