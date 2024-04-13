package com.wayfare.backend.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShortsWithComment{
    private String id;
    private String shortsUrl,description,userId,userName, thumbnailUrl, posterPictureUrl;
    private TourListing listing;
    private ArrayList<String> likes;
    private Date datePosted;
    private List<CommentWithUser> comments;

    public ShortsWithComment(String id, String shortsUrl, String userName,String userId, String description, Date datePosted, TourListing listing,ArrayList<String> likes, String thumbnailUrl, String posterPictureUrl, List<CommentWithUser> comments) {
        this.id = id;
        this.shortsUrl = shortsUrl;
        this.userId = userId;
        this.userName = userName;
        this.description = description;
        this.listing = listing;
        this.likes = likes;
        this.datePosted = datePosted;
        this.thumbnailUrl = thumbnailUrl;
        this.posterPictureUrl = posterPictureUrl;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getShortsUrl() {
        return shortsUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDescription() {
        return description;
    }

    public TourListing getListing() {
        return listing;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public Date getDatePosted() {
        return datePosted;
    }

 public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getPosterPictureUrl() {
        return posterPictureUrl;
    }

    public List<CommentWithUser> getComments() {
        return comments;
    }
}