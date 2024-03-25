package com.wayfare.backend.model;

import com.wayfare.backend.model.object.TimeRange;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;

@Document(collection = "tourListings")
public class TourListing {
    private String id;
    private String title;
    private String description;
    private Point location;
    private ArrayList<TimeRange> timeRangeList;
    private double adultPrice;
    private double childPrice;
    private int maxPax;
    private int minPax;
    private double rating;
    private int reviewCount;
    private String userId;

    public TourListing(
            String title,
            String description,
            Point location,
            ArrayList<TimeRange> timeRangeList,
            double adultPrice,
            double childPrice,
            int maxPax,
            int minPax,
            double rating,
            int reviewCount,
            String userId

    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.timeRangeList = timeRangeList;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
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

    public double getAdultPrice() {
        return adultPrice;
    }

    public double getChildPrice() {
        return childPrice;
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
}