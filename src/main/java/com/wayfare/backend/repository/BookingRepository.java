package com.wayfare.backend.repository;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.object.TimeRange;

import com.wayfare.backend.response.BookingResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findAllByListingId(String listingId);
    Booking findByListingId(String listingId);
    List<Booking> findAllByUserId(String userId);
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
            "{ $unwind : { path: $user }}",
            "{ $sort : { dateBooked: 1, \"bookingDuration.startTime\" : 1}}"
    })
    List<BookingResponse> findAllPastBookings(String userId);

    List<Booking> findByDateBookedAndBookingDuration(Date dateBooked, TimeRange bookingDuration);
}
