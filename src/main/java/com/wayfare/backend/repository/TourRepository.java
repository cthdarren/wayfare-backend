package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface TourRepository extends MongoRepository<TourListing, String> {
    List<TourListing> findAllByUserId(String userId);
    @Aggregation(pipeline = {
            "{ '$match': { 'id' : ?0 } }",
            "{ '$project': { 'userId': 1, '_id': 0 } }"
    })
    String findUserIdByListingId(String id);
    @Aggregation(pipeline = {
            "{ $match : { userId: ?0, rating: { $ne: 0 } } }",
            "{ $group : {_id : null, average: { $avg : $rating}}}"
    })
    Double avgScoreByUserId(String userId);


    @Aggregation(pipeline = {

            """
            { $lookup : from: 'bookings',
                                         localField: '_id',
                                         foreignField: 'listing._id',
                                         as: 'conflictingBookings',
                                         pipeline:
                                         [
                                           {$match: {
                                               $and:
                                           [
                                             {gte: [ '$dateBooked', '?0' ]},
                                             {lte: [ '$dateBooked', '?1' ]}
                                           ]
                                           }
                                           }] }
            """,
            "{ $unwind : { path: $timeRangeList }}",
            """
                     { $match:    $nor: [ {
                                            $and: [
                                            {
                                              forall: {
                                                $in: ['$conflictingBookings'],
                                                      $elemMatch: {
                                                        $or: [{
                                                          $and: [
                                                          { gte: [ '$bookingDuration.startTime', '$timeRangeList.startTime' ] },\s
                                                          { lte: [ '$bookingDuration.startTime', '$timeRangeList.endTime' ] }
                                                          ]
                                                        }]
                                                      } }
                                            }
                                            ]
                                          } ]}
            """

    })
    List<TourListing> findAvailableListingsByDateRange(Date startDate, Date endDate);



    //https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.geo-spatial
    // Find listings based on location
    List<TourListing> findByLocationNearAndMaxPaxGreaterThanEqualAndMinPaxLessThanEqualOrderByRatingDesc(Point location, Distance distance, Integer maxPax, Integer minPax);
}
