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
    private final Instant dateCreated;
    private final List<Review> reviews;
    private final List<TourListing> tours;
    private final List<BadgeEnum> badges;

    public ProfileResponse(String username, String aboutMe, String pictureUrl, Instant dateCreated, List<Review> reviews, List<TourListing> tours, List<BadgeEnum> badges) {
        this.username = username;
        this.aboutMe = aboutMe;
        this.pictureUrl = pictureUrl;
        this.dateCreated = dateCreated;
        this.reviews = reviews;
        this.tours = tours;
        this.badges = badges;
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
}
