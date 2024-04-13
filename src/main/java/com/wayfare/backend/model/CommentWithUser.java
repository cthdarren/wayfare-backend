package com.wayfare.backend.model;

import java.time.Instant;

public class CommentWithUser extends Comment{
    private User user;


    public CommentWithUser(String journeyId, String userId, String commentContent, Instant dateCreated, User user) {
        super(journeyId, userId, commentContent, dateCreated);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}

