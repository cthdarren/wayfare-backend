package com.wayfare.backend.model;

import com.wayfare.backend.model.object.PublicUserData;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "reviews")
public class Review {
    private String id;
    private String title;
    private Integer score;
    private String reviewContent;
    private final Instant dateCreated;
    private Instant dateModified;

    //reviewer
    private PublicUserData user;
    private String revieweeId;
    private TourListing listing;
    private String bookingId;

    public Review(String title, Integer score, String reviewContent, Instant dateCreated, Instant dateModified, PublicUserData user, TourListing listing, String bookingId, String revieweeId) {
        this.title = title;
        this.score = score;
        this.reviewContent = reviewContent;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.user= user;
        this.listing = listing;
        this.bookingId = bookingId;
        this.revieweeId = revieweeId;
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

    public Integer getScore() {
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

    public PublicUserData getUser() {
        return user;
    }

    public TourListing getListing() {
        return listing;
    }

    public String getRevieweeId() {
        return revieweeId;
    }

    public String getBookingId() {
        return bookingId;
    }
}
