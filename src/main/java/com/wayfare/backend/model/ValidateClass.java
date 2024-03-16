package com.wayfare.backend.model;

import java.util.Set;

public abstract class ValidateClass {
    private Set<String> errors;
    public ValidateClass(){}
    public void setErrors(Set<String> errors){this.errors = errors;}
    public boolean hasErrors(){return !errors.isEmpty();}
    public Set<String> getErrors(){return this.errors;}
    public abstract void validate();
}
