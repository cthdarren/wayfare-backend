package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

class CustomTourRepositoryImpl implements CustomTourRepository{
    private final MongoTemplate mongoTemplate;

    public CustomTourRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TourListing> findByLocationNearOrderByRating(Point location, Distance distance) {
        Aggregation aggregation = newAggregation(
                lookup("reviews", "id", "listingId", "reviews"),
                unwind("reviews"),
                group("id").avg("reviews.score").as("averageReviewScore"),
                sort(Sort.Direction.DESC, "averageReviewScore"),
                project("_id").and("averageReviewScore").as("averageReviewScore")
        );
        AggregationResults<TourListing> results = mongoTemplate.aggregate(aggregation, "tourListings", TourListing.class);
        return results.getMappedResults();
    }
}
