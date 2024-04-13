package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "comments")
public class Comment{
    private String id;
    private String journeyId;
    private String userId;
    private String commentContent;
    private Instant dateCreated;

    public Comment(String journeyId, String userId, String commentContent, Instant dateCreated) {
        this.journeyId = journeyId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.dateCreated = dateCreated;
    }

    public String getId(){
        return id;
    }

    public String getJourneyId(){
        return journeyId;
    }

    public String getUserId(){
        return userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

}

