package com.wayfare.backend.helper;

import org.springframework.data.rest.core.ValidationErrors;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public Pattern pattern;
    public String errMsg;
    public String input;
    public Validator(Pattern pattern, String errMsg, String input){
        this.pattern = pattern;
        this.input = input;
    }
    public boolean validateRegex(){
        Matcher matcher = this.pattern.matcher(this.input);
        if (!matcher.find())
            throw new IllegalArgumentException();
        return true;
    }
}
