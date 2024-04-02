package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "otp")
public class Otp {

    private String id;
    private String otpString;
    private String userId;
    private Instant creationTime;

    public Otp(String otpString, String userId){
        this.otpString = otpString;
        this.userId = userId;
        creationTime = Instant.now();
    }

    public String getOtpString() {
        return otpString;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreationTime() {
        return creationTime;
    }
}
