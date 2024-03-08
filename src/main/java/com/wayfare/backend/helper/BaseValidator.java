package com.wayfare.backend.helper;

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
    public boolean validateRegex(){
        Matcher matcher = this.pattern.matcher(this.input);
        if (!matcher.find()) {
            System.out.println(this.input);
            System.out.println(this.errMsg);
            System.out.println(this.input);
            throw new IllegalArgumentException(errMsg);
        }
        return true;
    }
}
