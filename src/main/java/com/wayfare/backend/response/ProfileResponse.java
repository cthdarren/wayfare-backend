package com.wayfare.backend.response;

import com.wayfare.backend.model.*;

import java.time.Instant;
import java.util.List;

public class ProfileResponse {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String aboutMe;
    private final String pictureUrl;
    private final List<String> languagesSpoken;
    private final List<BadgeEnum> badges;
    private final Double avgScore;
    private final Integer reviewCount;
    private final RoleEnum role;
    private final List<Review> reviews;
    private final List<TourListing> tours;
    private final Instant dateCreated;
    private final boolean isVerified;

    public ProfileResponse(String username, String firstName, String lastName, String aboutMe, String pictureUrl, List<String> languagesSpoken, List<BadgeEnum> badges, double avgScore, int reviewCount, RoleEnum role, List<Review> reviews, List<TourListing> tours, Instant dateCreated, boolean isVerified) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.aboutMe = aboutMe;
        this.pictureUrl = pictureUrl;
        this.languagesSpoken = languagesSpoken;
        this.badges = badges;
        this.avgScore = avgScore;
        this.reviewCount = reviewCount;
        this.role = role;
        this.reviews = reviews;
        this.tours = tours;
        this.dateCreated = dateCreated;
        this.isVerified = isVerified;
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

    public RoleEnum getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public boolean isVerified(){
        return isVerified;
    }
}
