package com.wayfare.backend.model;

import java.time.Instant;

public class Review {
    private String id;
    private String title;
    private String score;
    private String reviewContent;
    private Instant dateCreated;
    private Instant dateModified;

    private String userId;
    private String listingId;
}
