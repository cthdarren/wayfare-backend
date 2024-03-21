package com.wayfare.backend.request;

public record CreateReviewRequest(String title, int score, String reviewContent, String listingId) {
}
