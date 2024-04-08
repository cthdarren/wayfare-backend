package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "currencies")
public class Currency{
    private String id;
    private Object data;
    private Instant lastUpdate;
    public Currency(Object data){
        this.data = data;
    }
    public String getId(){
        return id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object currencyData) {
        this.data = currencyData;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    class dataMap{
        public Object map;
    }
}

