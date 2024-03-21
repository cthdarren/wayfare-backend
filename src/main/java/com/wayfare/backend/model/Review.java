package com.wayfare.backend.model;

import java.time.Instant;

public class Review {
    private String id;
    private String title;
    private int score;
    private String reviewContent;
    private final Instant dateCreated;
    private Instant dateModified;

    private String userId;
    private String listingId;

    public Review(String title, int score, String reviewContent, Instant dateCreated, Instant dateModified, String userId,  String listingId) {
        this.title = title;
        this.score = score;
        this.reviewContent = reviewContent;
        this.dateCreated = dateModified;
        this.dateModified = dateCreated;
        this.userId = userId;
        this.listingId = listingId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public String getUserId() {
        return userId;
    }

    public String getListingId() {
        return listingId;
    }


}
