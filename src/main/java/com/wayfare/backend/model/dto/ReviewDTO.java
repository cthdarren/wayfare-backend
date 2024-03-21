package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.validator.*;

import java.time.Instant;
import java.util.Objects;

//only used for the creation of new users
public class ReviewDTO extends ValidateClass {
    private String title;
    private int score;
    private String reviewContent;
    private String listingId;

    public ReviewDTO(String title, int score, String reviewContent){
        this.title = title;
        this.score = score;
        this.reviewContent = reviewContent;
    }

    public String getTitle() {
        return title;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public int getScore() {
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
}
