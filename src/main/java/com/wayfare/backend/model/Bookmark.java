package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookmarks")
public class Bookmark {

    private String id;
    private TourListing listing;
    private String userId;

    public Bookmark(TourListing listing, String userId){
        this.listing = listing;
        this.userId = userId;
    }

    public TourListing getListing() {
        return listing;
    }

    public String getUserId() {
        return userId;
    }
}
