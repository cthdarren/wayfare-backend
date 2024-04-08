package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.*;

import java.time.Instant;
import java.util.Objects;

//only used for the creation of new users
public class ReviewDTO extends ValidateClass {
    private final String title;
    private final Integer score;
    private final String reviewContent;
    private final String listingId;
    private final String bookingId;

    public ReviewDTO(String title, Integer score, String reviewContent, String listingId, String bookingId){
        this.title = title;
        this.score = score;
        this.reviewContent = reviewContent;
        this.listingId = listingId;
        this.bookingId = bookingId;
    }

    public String getTitle() {
        return title;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public Integer getScore() {
        return score;
    }

    public String getListingId() {
        return listingId;
    }
    @Override
    public void validate() {
        if(this.score < 0 || this.score > 5){addErrors("Score must be between 1-5 inclusive");}
        getErrors().remove(null);
    }

    public String getBookingId() {
        return bookingId;
    }
}
