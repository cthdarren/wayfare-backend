package com.wayfare.backend.exception;

import java.util.Set;

public class RegistrationFormatException extends Exception{
    private Set<String> errors;
    public RegistrationFormatException(Set<String> errors){
        super();
        this.errors = errors;
    }

    public Set<String> getErrors(){
        return errors;
    }
}
