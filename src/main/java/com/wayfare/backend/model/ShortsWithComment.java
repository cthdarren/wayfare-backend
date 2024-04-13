package com.wayfare.backend.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShortsWithComment extends Shorts{
    private List<CommentWithUser> comments;

    public ShortsWithComment(String shortsUrl, String userName,String userId, String description, Date datePosted, TourListing listing,ArrayList<String> likes, String thumbnailUrl, List<CommentWithUser> comments) {
        super(shortsUrl, userName, userId, description, datePosted, listing, likes, thumbnailUrl);
        this.comments = comments;
    }

    public List<CommentWithUser> getComments() {
        return comments;
    }
}
