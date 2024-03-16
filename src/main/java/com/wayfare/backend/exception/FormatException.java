package com.wayfare.backend.exception;

import java.util.Set;

public class FormatException extends Exception{
    private Set<String> errors;
    public FormatException(Set<String> errors){
        super();
        this.errors = errors;
    }

    public Set<String> getErrors(){
        return errors;
    }
}
