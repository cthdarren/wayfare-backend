package com.wayfare.backend.model.dto;


import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.model.ValidateClass;
import org.springframework.data.geo.Point;

import java.time.Instant;
import java.util.ArrayList;

public class TourListingDTO extends ValidateClass {
    private final String title;
    private final String description;
    private final Point location;
    private ArrayList<TimeRange> timeRangeList;
    private final Double adultPrice;
    private final Double childPrice;
    private final Integer maxPax;
    private final Integer minPax;

    public TourListingDTO(
            String title,
            String description,
            Point location,
            ArrayList<TimeRange> timeRangeList,
            Double adultPrice,
            Double childPrice,
            Integer maxPax,
            Integer minPax
    ){
        this.title = title;
        this.description = description;
        this.location = location;
        setTimeRangeList(timeRangeList);
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.maxPax = maxPax;
        this.minPax = minPax;
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

    public Double getAdultPrice() {
        return adultPrice;
    }

    public Double getChildPrice() {
        return childPrice;
    }

    public Integer getMaxPax() {
        return maxPax;
    }

    public Integer getMinPax() {
        return minPax;
    }

    @Override
    public void validate() {
        if (getTitle() == null || getDescription() ==null || getLocation() == null || getTimeRangeList() == null || getAdultPrice() == null || getChildPrice() == null || getMinPax() == null || getMaxPax() == null)
            addErrors("Missing fields in json");
        getErrors().remove(null);
    }
}
