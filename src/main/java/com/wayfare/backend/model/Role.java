package com.wayfare.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class Role {

    private RoleEnum name;

    public Role() {
    }

    public Role(RoleEnum name) {
        this.name = name;
    }

    public RoleEnum getName() {
        return name;
    }
}

