package com.wayfare.backend.model.dto;


import com.wayfare.backend.model.CategoryEnum;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.model.ValidateClass;
import org.springframework.data.geo.Point;

import java.util.ArrayList;

public class TourListingDTO extends ValidateClass {
    private final String title;
    private final String description;
    private ArrayList<String> thumbnailUrls;
    private final CategoryEnum category;
    private final Point location;
    private ArrayList<TimeRange> timeRangeList;
    private double price;
    private final Integer maxPax;
    private final Integer minPax;

    public TourListingDTO(
            String title,
            String description,
            ArrayList<String> thumbnailUrls,
            CategoryEnum category,
            Point location,
            ArrayList<TimeRange> timeRangeList,
            Double price,
            Integer maxPax,
            Integer minPax
    ){
        this.title = title;
        this.description = description;
        this.thumbnailUrls = thumbnailUrls;
        this.location = location;
        this.category = category;
        setTimeRangeList(timeRangeList);
        setPrice(price);
        this.maxPax = maxPax;
        this.minPax = minPax;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setThumbnailUrls(ArrayList<String> thumbnailUrls) {
        if (thumbnailUrls.isEmpty()){
            this.thumbnailUrls = new ArrayList<String>();
        }
        else{
            this.thumbnailUrls = thumbnailUrls;
        }
    }
    public Point getLocation() {
        return location;
    }

    public ArrayList<TimeRange> getTimeRangeList() {
        return timeRangeList;
    }
    public void setTimeRangeList(ArrayList<TimeRange> timeRangeList) {
        if (timeRangeList == null){
            addErrors("timeRangeList is null");
        }
        else if (timeRangeList.isEmpty()){
            addErrors("Minimum 1 time slot required");
        }
        else{
            this.timeRangeList = timeRangeList;
        }
        //TODO validate when time ranges overlap
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double value) {
        if (value == null){
            addErrors("Price cannot be null");
        }
        else if(value <= 0){
            addErrors("Price must be more than 0");
        }
        else{
            this.price = value;
        }
    }

    public Integer getMaxPax() {
        return maxPax;
    }

    public Integer getMinPax() {
        return minPax;
    }

    @Override
    public void validate() {
        if (getTitle() == null || getDescription() ==null || getCategory() == null || getLocation() == null || getTimeRangeList() == null || getPrice() == null || getMinPax() == null || getMaxPax() == null)
            addErrors("Missing fields in json");
        getErrors().remove(null);
    }

    public ArrayList<String> getThumbnailUrls() {
        return thumbnailUrls;
    }

    public CategoryEnum getCategory() {
        return category;
    }
}
