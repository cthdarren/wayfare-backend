package com.wayfare.backend.model;

import com.wayfare.backend.exception.FormatException;

import java.util.HashSet;
import java.util.Set;

public abstract class ValidateClass {
    private Set<String> errors = new HashSet<>();
    public ValidateClass(){}
    public void addErrors(String errors){this.errors.add(errors);}
    public boolean hasErrors(){return !errors.isEmpty();}
    public Set<String> getErrors(){return this.errors;}
    public abstract void validate();
}
