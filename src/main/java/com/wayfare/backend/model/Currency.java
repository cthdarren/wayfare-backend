package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "currencies")
public class Currency{
    private String id;
    public Currency(){

    }
    public String getId(){
        return id;
    }
}

