package com.wayfare.backend.model;

public class VerifyURL {
    private String url;
    private String userID;

    public VerifyURL(String url, String userID){
        this.url = url;
        this.userID = userID;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
