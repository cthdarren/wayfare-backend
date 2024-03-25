package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TourRepository extends MongoRepository<TourListing, String> {
    List<TourListing> findAllByUserId(String userId);
//    List<TourListing> findByTourName(String tourName);

    //https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.geo-spatial
    // Find listings based on location
    List<TourListing> findByLocationNearOrderByRating(Point location, Distance distance);
}
