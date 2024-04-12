package com.wayfare.backend.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "shorts")
public class Shorts {
//    private String id;
    private String shortsUrl,description,userId,userName;
    private TourListing listing;
    private ArrayList<String> likes;
    private Date datePosted;

    public Shorts(String shortsUrl, String userName,String userId, String description, Date datePosted, TourListing listing,ArrayList<String> likes) {
        this.shortsUrl = shortsUrl;
        this.userId = userId;
        this.userName = userName;
        this.description = description;
        this.listing = listing;
        this.likes = likes;
        this.datePosted = datePosted;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getShortsUrl() {
        return shortsUrl;
    }

    public void setShortsUrl(String shortsUrl) {
        this.shortsUrl = shortsUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TourListing getListing() {
        return listing;
    }

    public void setListing(TourListing listing) {
        this.listing = listing;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void addLike(String like) {
        if (this.likes == null) {
            this.likes = new ArrayList<>();
        }
        if (!this.likes.contains(like)) {
            this.likes.add(like);
        }
    }
    public void removeLike(String like) {
        if (this.likes != null) {
            this.likes.remove(like);
        }
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }
}
