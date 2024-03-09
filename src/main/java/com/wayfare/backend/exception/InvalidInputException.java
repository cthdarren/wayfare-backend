package com.wayfare.backend.exception;

import java.util.ArrayList;

public class InvalidInputException extends Exception{
    private ArrayList<String> errStrings;
    public InvalidInputException(ArrayList<String> errStrings){
        this.errStrings = errStrings;
    }

    @Override
    public String getMessage() {
        return String.join(",", errStrings);
    }
}
