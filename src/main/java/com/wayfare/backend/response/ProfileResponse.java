package com.wayfare.backend.response;

import com.wayfare.backend.model.BadgeEnum;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;

import java.time.Instant;
import java.util.List;

public class ProfileResponse {
    private final String username;
    private final String aboutMe;
    private final String pictureUrl;
    private final List<BadgeEnum> badges;
    private final Double avgScore;
    private final Integer reviewCount;
    private final List<Review> reviews;
    private final List<TourListing> tours;

    private final Instant dateCreated;

    public ProfileResponse(String username, String aboutMe, String pictureUrl, List<BadgeEnum> badges, double avgScore, int reviewCount, List<Review> reviews, List<TourListing> tours, Instant dateCreated) {
        this.username = username;
        this.aboutMe = aboutMe;
        this.pictureUrl = pictureUrl;
        this.badges = badges;
        this.avgScore = avgScore;
        this.reviewCount = reviewCount;
        this.reviews = reviews;
        this.tours = tours;
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public List<TourListing> getTours() {
        return tours;
    }

    public List<BadgeEnum> getBadges() {
        return badges;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }
}
