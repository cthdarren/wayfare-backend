package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TourRepository extends MongoRepository<TourListing, String> {
    List<TourListing> findAllByUserId(String userId);
    @Aggregation(pipeline = {
            "{ $match : { userId: ?0}}",
            "{ $group : {_id : null, average: { $avg : $rating}}}"
    })
    Double avgScoreByUserId(String userId);



    //https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.geo-spatial
    // Find listings based on location
    List<TourListing> findByLocationNearAndMaxPaxGreaterThanEqualAndMinPaxLessThanEqualOrderByRatingDesc(Point location, Distance distance, Integer maxPax, Integer minPax);
}
