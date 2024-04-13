package com.wayfare.backend.model;

import java.time.Instant;

import com.wayfare.backend.model.object.PublicUserData;

public class CommentWithUser extends Comment{
    private PublicUserData user;


    public CommentWithUser(String journeyId, String userId, String commentContent, Instant dateCreated, PublicUserData user) {
        super(journeyId, userId, commentContent, dateCreated);
        this.user = user;
    }

    public PublicUserData getUser() {
        return user;
    }

}

