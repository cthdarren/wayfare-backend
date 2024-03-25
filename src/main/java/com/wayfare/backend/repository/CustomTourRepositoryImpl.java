package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

class CustomTourRepositoryImpl implements CustomTourRepository{
    private final MongoTemplate mongoTemplate;

    public CustomTourRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TourListing> findByLocationNearOrderByRating(Point location, Distance distance) {
     class test{
        final String $toString;
        test(){$toString = "$_id";}
    }
        Aggregation aggregation = newAggregation(
                addFields().addField("listingId").withValue(new test()).build(),
                lookup("review", "listingId", "listingId", "result"),
                unwind("result"),
                group("$listingId")
                        .avg("$result.score").as("rating")
                        .first("id").as("id")
                        .first("title").as("title")
                        .first("description").as("description")
                        .first("location").as("location")
                        .first("timeRangeList").as("timeRangeList")
                        .first("adultPrice").as("adultPrice")
                        .first("childPrice").as("childPrice")
                        .first("maxPax").as("maxPax")
                        .first("minPax").as("minPax")
                        .first("userId").as("userId"),
                sort(Sort.Direction.DESC, "rating")
        );
        AggregationResults<TourListing> results = mongoTemplate.aggregate(aggregation, "tourListings", TourListing.class);
        return results.getMappedResults();
    }
}

//[
//        {
//$addFields:
//        /**
//         * newField: The new field name.
//         * expression: The new field expression.
//         */
//        {
//listingId: {
//$toString: "$_id",
//        },
//        },
//        },
//        {
//$lookup:
//        /**
//         * from: The target collection.
//         * localField: The local join field.
//         * foreignField: The target join field.
//         * as: The name for the results.
//         * pipeline: Optional pipeline to run on the foreign collection.
//         * let: Optional variables to use in the pipeline field stages.
//         */
//        {
//from: "review",
//localField: "listingId",
//foreignField: "listingId",
//as: "reviews",
//        },
//        },
//        {
//$unwind:
//        /**
//         * path: Path to the array field.
//         * includeArrayIndex: Optional name for index.
//         * preserveNullAndEmptyArrays: Optional
//         *   toggle to unwind null and empty values.
//         */
//        {
//path: "$reviews",
//        },
//        },
//        {
//$group:
//        /**
//         * _id: The id of the group.
//         * fieldN: The first field name.
//         */
//        {
//_id: "$listingId",
//averageReviewScore: {
//$avg: "$reviews.score",
//        },
//        },
//        },
//        ]