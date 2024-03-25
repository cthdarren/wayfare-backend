package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.util.List;

public interface CustomTourRepository {
    List<TourListing> findByLocationNearOrderByRating(Point location, Distance distance);
}
