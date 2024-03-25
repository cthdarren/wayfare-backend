package com.wayfare.backend.model;

import com.wayfare.backend.model.object.TimeRange;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "tourListings")
public class TourListing {
    private String id;
    private String title;
    private String description;
    private ArrayList<String> thumbnailUrls;
    private CategoryEnum category;
    private Point location;
    private ArrayList<TimeRange> timeRangeList;
    private Double price;
    private int maxPax;
    private int minPax;
    private double rating;
    private int reviewCount;
    private String userId;

    public TourListing(
            String title,
            String description,
            ArrayList<String> thumbnailUrls,
            CategoryEnum category,
            Point location,
            ArrayList<TimeRange> timeRangeList,
            Double price,
            int maxPax,
            int minPax,
            double rating,
            int reviewCount,
            String userId

    ) {
        this.title = title;
        this.description = description;
        this.thumbnailUrls = thumbnailUrls;
        this.category = category;
        this.location = location;
        this.timeRangeList = timeRangeList;
        this.price = price;
        this.maxPax = maxPax;
        this.minPax = minPax;
        this.userId = userId;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Point getLocation() {
        return location;
    }

    public ArrayList<TimeRange> getTimeRangeList() {
        return timeRangeList;
    }

    public Double getPrice() {
        return price;
    }

    public int getMaxPax() {
        return maxPax;
    }

    public int getMinPax() {
        return minPax;
    }

    public String getUserId() {
        return userId;
    }

    public double getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public ArrayList<String> getThumbnailUrls() {
        return thumbnailUrls;
    }

    public CategoryEnum getCategory() {
        return category;
    }
}