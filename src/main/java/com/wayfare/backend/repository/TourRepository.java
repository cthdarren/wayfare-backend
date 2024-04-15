package com.wayfare.backend.repository;

import com.wayfare.backend.model.TourListing;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
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
                      {
                        $match: {
                          location: {
                            $geoWithin: {
                              $centerSphere: [
                                [?0, ?1],
                                ?2,
                              ],
                            },
                          },
                        },
                      },""",
            """
                      {
                        $lookup: {
                          from: "bookings",
                          localField: "_id",
                          foreignField: "listing._id",
                          as: "conflictingBookings",
                          pipeline: [
                            {
                              $match: {
                                $expr: {
                                  $and: [
                                    {
                                      $gte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?3",
                                          },
                                        },
                                      ],
                                    },
                                    {
                                      $lte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?4",
                                          },
                                        },
                                      ],
                                    },
                                  ],
                                },
                              },
                            },
                          ],
                        },
                      },""",
            """
                      {
                        $addFields: {
                          conflictingBookingsCount: {
                            $size: "$conflictingBookings",
                          },
                          expectedBookingsCount: {
                            $multiply: [
                              {
                                $size: "$timeRangeList",
                              },
                              {
                                $add: [
                                  {
                                    $divide: [
                                      {
                                        $subtract: [
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?4",
                                            },
                                          },
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?3",
                                            },
                                          },
                                        ],
                                      },
                                     86400000
                                    ],
                                  },
                                  1,
                                ],
                              },
                            ],
                          },
                        },
                      },"""
            ,
                      """
                      {
                        $match: {
                          $expr: {
                            $ne: [
                              "$expectedBookingsCount",
                              "$conflictingBookingsCount",
                            ],
                          },
                        },
                      },
                    """,
            """
                    {
                      "$sort": {
                        "listing.rating": -1
                      }
                    }
                    """
    })
    List<TourListing> findWithAllParams(Double longitude, Double latitude, Double radianDistance, String instantStartDateSearch, String instantEndDateSearch);
    @Aggregation({
            """
                    {
                      "$match": {
                        "$and": [
                          { "minPax": { "$lte": ?0 } },
                          { "maxPax": { "$gte": ?0 } }
                        ]
                      }
                    }
                    """,
            """
                     {
                        $lookup: {
                          from: "bookings",
                          localField: "_id",
                          foreignField: "listing._id",
                          as: "conflictingBookings",
                          pipeline: [
                            {
                              $match: {
                                $expr: {
                                  $and: [
                                    {
                                      $gte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?1",
                                          },
                                        },
                                      ],
                                    },
                                    {
                                      $lte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?2",
                                          },
                                        },
                                      ],
                                    },
                                  ],
                                },
                              },
                            },
                          ],
                        },
                      },""",
            """
                      {
                        $addFields: {
                          conflictingBookingsCount: {
                            $size: "$conflictingBookings",
                          },
                          expectedBookingsCount: {
                            $multiply: [
                              {
                                $size: "$timeRangeList",
                              },
                              {
                                $add: [
                                  {
                                    $divide: [
                                      {
                                        $subtract: [
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?2",
                                            },
                                          },
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?1",
                                            },
                                          },
                                        ],
                                      },
                                      86400000
                                    ],
                                  },
                                  1,
                                ],
                              },
                            ],
                          },
                        },
                      },"""
            ,"""
                      {
                        $match: {
                          $expr: {
                            $ne: [
                              "$expectedBookingsCount",
                              "$conflictingBookingsCount",
                            ],
                          },
                        },
                      },
                    """,
            """
                    {
                      "$sort": {
                        "listing.rating": -1
                      }
                    }
                    """
    })
    List<TourListing> findAvailableListingsByPaxAndDateRange(Integer numPax, String startDate, String endDate);

    @Aggregation({
            """
                     {
                        $lookup: {
                          from: "bookings",
                          localField: "_id",
                          foreignField: "listing._id",
                          as: "conflictingBookings",
                          pipeline: [
                            {
                              $match: {
                                $expr: {
                                  $and: [
                                    {
                                      $gte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?0",
                                          },
                                        },
                                      ],
                                    },
                                    {
                                      $lte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?1",
                                          },
                                        },
                                      ],
                                    },
                                  ],
                                },
                              },
                            },
                          ],
                        },
                      },""",
            """
                      {
                        $addFields: {
                          conflictingBookingsCount: {
                            $size: "$conflictingBookings",
                          },
                          expectedBookingsCount: {
                            $multiply: [
                              {
                                $size: "$timeRangeList",
                              },
                              {
                                $add: [
                                  {
                                    $divide: [
                                      {
                                        $subtract: [
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?1",
                                            },
                                          },
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?0",
                                            },
                                          },
                                        ],
                                      },
                                      86400000
                                    ],
                                  },
                                  1,
                                ],
                              },
                            ],
                          },
                        },
                      },"""
            ,"""
                      {
                        $match: {
                          $expr: {
                            $ne: [
                              "$expectedBookingsCount",
                              "$conflictingBookingsCount",
                            ],
                          },
                        },
                      },
                    """,
            """
                    {
                      "$sort": {
                        "listing.rating": -1
                      }
                    }
                    """
    })
    List<TourListing> findAvailableListingsByDateRange(String startDate, String endDate);

    @Aggregation({
            """
                  {
                    $match: {
                      location: {
                        $geoWithin: {
                          $centerSphere: [
                            [?0, ?1],
                            ?2,
                          ],
                        },
                      },
                    },
                  },""",
            """
                     {
                        $lookup: {
                          from: "bookings",
                          localField: "_id",
                          foreignField: "listing._id",
                          as: "conflictingBookings",
                          pipeline: [
                            {
                              $match: {
                                $expr: {
                                  $and: [
                                    {
                                      $gte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?3",
                                          },
                                        },
                                      ],
                                    },
                                    {
                                      $lte: [
                                        "$dateBooked",
                                        {
                                          $dateFromString: {
                                            dateString:
                                              "?4",
                                          },
                                        },
                                      ],
                                    },
                                  ],
                                },
                              },
                            },
                          ],
                        },
                      },""",
            """
                      {
                        $addFields: {
                          conflictingBookingsCount: {
                            $size: "$conflictingBookings",
                          },
                          expectedBookingsCount: {
                            $multiply: [
                              {
                                $size: "$timeRangeList",
                              },
                              {
                                $add: [
                                  {
                                    $divide: [
                                      {
                                        $subtract: [
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?4",
                                            },
                                          },
                                          {
                                            $dateFromString: {
                                              dateString:
                                                "?3",
                                            },
                                          },
                                        ],
                                      },
                                      86400000
                                    ],
                                  },
                                  1,
                                ],
                              },
                            ],
                          },
                        },
                      },"""
            ,"""
                      {
                        $match: {
                          $expr: {
                            $ne: [
                              "$expectedBookingsCount",
                              "$conflictingBookingsCount",
                            ],
                          },
                        },
                      },
                    """,
            """
                    {
                      "$sort": {
                        "listing.rating": -1
                      }
                    }
                    """
    })
    List<TourListing> findAvailableListingsByLocAndDateRange(Double longitude, Double latitude, Double radiansDistance, String startDate, String endDate);

    //https://docs.spring.io/spring-data/mongodb/reference/mongodb/repositories/query-methods.html#mongodb.repositories.queries.geo-spatial
    // Find listings based on location
    List<TourListing> findByLocationNearAndMaxPaxGreaterThanEqualAndMinPaxLessThanEqualOrderByRatingDesc(Point location, Distance distance, Integer maxPax, Integer minPax);
    List<TourListing> findByMaxPaxGreaterThanEqualAndMinPaxLessThanEqualOrderByRatingDesc(Integer maxPax, Integer minPax);

    List<TourListing> findByLocationNearOrderByRatingDesc(Point location, Distance distance);
}
