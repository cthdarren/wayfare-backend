package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verifyURL")
public class VerifyURL {

    private String id;
    private String url;
    private String username;

    public VerifyURL(String url, String username){
        this.url = url;
        this.username = username;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
