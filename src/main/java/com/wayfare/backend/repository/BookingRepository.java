package com.wayfare.backend.repository;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.object.TimeRange;

import com.wayfare.backend.response.BookingResponse;
import com.wayfare.backend.response.BookingResponseWithReview;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findAllByListingId(String listingId);
    Booking findByListingId(String listingId);
    List<Booking> findAllByUserId(String userId);

    @Aggregation(pipeline = {
            "{ $match : { listingId: ?0 } }",
            "{ $project: { dateBooked: 1, _id: 0 } }"
    })
    List<Date> findBookedDatesByListingId(String listingId);

    @Aggregation(pipeline = {
            "{ $match : { listingId: { $in : ?0 }, dateBooked: { $gte : ?1, $lt: ?2 }}}", """
            { $lookup: {
                 from: "users",
                 let: {
                   searchId: {
                     $toObjectId: "$userId",
                   },
                 },
                 pipeline: [
                   {
                     $match: {
                       $expr: {
                         $eq: ["$_id", "$$searchId"],
                       },
                     },
                   },
                   {
                     $project: {
                       username: 1,
                       pictureUrl: 1,
                     },
                   },
                 ],
                 as: "user",
               },
             }
            """,
            "{ $unwind : { path: $user }}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}",
    })
    List<BookingResponse> findBookingsWithinDay(List<String> listingId, Date beginOfDay, Date startOfNextDay);

    @Aggregation(pipeline = {
            "{ $match : { listingId: { $in : ?0 }, dateBooked: { $gte : ?1, $lt: ?2 }}}", """
            { $lookup: {
                 from: "users",
                 let: {
                   searchId: {
                     $toObjectId: "$userId",
                   },
                 },
                 pipeline: [
                   {
                     $match: {
                       $expr: {
                         $eq: ["$_id", "$$searchId"],
                       },
                     },
                   },
                   {
                     $project: {
                       username: 1,
                       pictureUrl: 1,
                     },
                   },
                 ],
                 as: "user",
               },
             }
            """,
            "{ $unwind : { path: $user }}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}"
    })
    List<BookingResponse> findBookingsWithinWeek(List<String> listingId, Date beginOfWeek, Date startOfNextWeek);

    @Aggregation(pipeline = {
            "{ $match : { listingId: { $in : ?0 }, dateBooked: { $gte : ?1, $lt: ?2 }}}",
            """
           { $lookup: {
                from: "users",
                let: {
                  searchId: {
                    $toObjectId: "$userId",
                  },
                },
                pipeline: [
                  {
                    $match: {
                      $expr: {
                        $eq: ["$_id", "$$searchId"],
                      },
                    },
                  },
                  {
                    $project: {
                      username: 1,
                      pictureUrl: 1,
                    },
                  },
                ],
                as: "user",
              },
            }
           """,
            "{ $unwind : { path: $user }}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}"
    })
    List<BookingResponse> findBookingsWithinMonth(List<String> listingId, Date beginOfMonth, Date startOfNextMonth);

    @Aggregation(pipeline = {
            //lmao sorry for the eye cancer but i copy-pasted this from mongodb after crafting the aggregation myself
            "{ $match : { userId: ?0, dateBooked:{ $gte : new Date()}}}",
            """
            { $lookup: {
                 from: "users",
                 let: {
                   searchId: {
                     $toObjectId: "$listing.userId",
                   },
                 },
                 pipeline: [
                   {
                     $match: {
                       $expr: {
                         $eq: ["$_id", "$$searchId"],
                       },
                     },
                   },
                   {
                     $project: {
                       username: 1,
                       pictureUrl: 1,
                     },
                   },
                 ],
                 as: "user",
               },
             }
            """,
            "{ $unwind : { path: $user }}",
            "{ $sort : { dateBooked: 1, \"bookingDuration.startTime\" : 1}}"
    })
    List<BookingResponse> findAllUpcomingBookings(String userId);
    @Aggregation(pipeline = {
            //lmao sorry for the eye cancer but i copy-pasted this from mongodb after crafting the aggregation myself
            //YOOO I GOT AN EVEN LONGER ONE
            "{ $match : { userId: ?0, dateBooked:{ $lt : new Date()}}}",
            """
        { $lookup: {
                 from: "users",
                 let: {
                   searchId: {
                     $toObjectId: "$listing.userId",
                   },
                 },
                 pipeline: [
                   {
                     $match: {
                       $expr: {
                         $eq: ["$_id", "$$searchId"],
                       },
                     },
                   },
                   {
                     $project: {
                       username: 1,
                       pictureUrl: 1,
                     },
                   },
                 ],
                 as: "user",
               },
             }
            """,
            "{ $unwind : { path: \"$user\" }}",
            "{ $sort : { dateBooked: -1, \"bookingDuration.startTime\" : 1}}",
            """
                    {
                        $lookup: {
                          from: "reviews",
                          let: {
                            listingId: "$listing._id",
                            userId: {
                              $toObjectId: "$userId",
                            },
                          },
                          pipeline: [
                            {
                              $match: {
                                $expr: {
                                  $and: [
                                    {
                                      $eq: [
                                        "$listing._id",
                                        "$$listingId",
                                      ],
                                    },
                                    {
                                      $eq: ["$user._id", "$$userId"],
                                    },
                                  ],
                                },
                              },
                            },
                            {
                              $limit: 1,
                            },
                            {
                              $project: {
                                _id: 0,
                                reviewed: {
                                  $literal: true,
                                }, 
                              },
                            },
                          ],
                          as: "reviews",
                        },
                      }
                    """,
            "{ $addFields: { reviewed: { $cond: { if: { $gt: [{ $size: \"$reviews\" }, 0] }, then: true, else: false } } }}"
    })
    List<BookingResponseWithReview> findAllPastBookings(String userId);

    List<Booking> findByDateBookedAndBookingDuration(Date dateBooked, TimeRange bookingDuration);
}
