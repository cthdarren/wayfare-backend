package com.wayfare.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "currencies")
public class Currency{

    private String name;
    private Double exchangeRate;

    public Currency(String name, Double exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public String getName() {
        return name;
    }

    public Double getExchangeRate(){
        return exchangeRate;
    }
}

