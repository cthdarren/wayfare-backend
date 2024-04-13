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
    private ArrayList<String> thumbnailUrls;
    private CategoryEnum category;
    private Point location;
    private String region;
    private ArrayList<TimeRange> timeRangeList;
    private Double price;
    private Integer maxPax;
    private Integer minPax;
    private Double rating;
    private Integer reviewCount;
    private String userId;
    private final Instant dateCreated;

    public TourListing(
            String title,
            String description,
            ArrayList<String> thumbnailUrls,
            CategoryEnum category,
            Point location,
            String region,
            ArrayList<TimeRange> timeRangeList,
            Double price,
            int maxPax,
            int minPax,
            double rating,
            int reviewCount,
            String userId,
            Instant dateCreated

    ) {
        this.title = title;
        this.description = description;
        this.thumbnailUrls = thumbnailUrls;
        this.category = category;
        this.location = location;
        this.region = region;
        this.timeRangeList = timeRangeList;
        this.price = price;
        this.maxPax = maxPax;
        this.minPax = minPax;
        this.userId = userId;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){this.title = title;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){this.description = description;}

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {this.location = location;}

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
    public ArrayList<TimeRange> getTimeRangeList() {
        return timeRangeList;
    }

    public void setTimeRangeList(ArrayList<TimeRange> timeRangeList) {this.timeRangeList = timeRangeList;}

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {this.price = price;}

    public void setMaxPax(Integer maxPax) {this.maxPax = maxPax;}

    public void setMinPax(Integer minPax) {this.minPax = minPax;}

    public Integer getMaxPax() {
        return maxPax;
    }

    public Integer getMinPax() {
        return minPax;
    }

    public String getUserId() {
        return userId;
    }

    public Double getRating() {
        return rating;
    }
    public void setRating(Double value){
        this.rating = value;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer value){
        this.reviewCount = value;
    }

    public ArrayList<String> getThumbnailUrls() {
        return thumbnailUrls;
    }

    public void setThumbnailUrls(ArrayList<String> thumbnailUrls) {this.thumbnailUrls = thumbnailUrls;}

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {this.category = category;}

    public Instant getDateCreated() {
        return dateCreated;
    }



}