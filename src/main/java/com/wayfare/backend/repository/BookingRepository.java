package com.wayfare.backend.repository;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.object.TimeRange;

import com.wayfare.backend.response.BookingResponse;
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

    // Get the current date at the start of the day
    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();  // 00:00:00 at the beginning of today
    Date beginOfDay = Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());

    // Get the start of the next day
    LocalDateTime endOfDay = startOfDay.plusDays(1);
    Date startOfNextDay = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    @Aggregation(pipeline = {
            "{ $match : { listingId: ?0, dateBooked: { $gte : ?1, $lt: ?2 }}}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}",
    })
    List<Booking> findBookingsWithinDay(String listingId, Date beginOfDay, Date startOfNextDay);

    // Get the start of the week (assuming Monday as the first day)
    LocalDateTime startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay();
    Date beginOfWeek = Date.from(startOfWeek.atZone(ZoneId.systemDefault()).toInstant());

    // Get the start of the next week
    LocalDateTime endOfWeek = startOfWeek.plusWeeks(1);
    Date startOfNextWeek = Date.from(endOfWeek.atZone(ZoneId.systemDefault()).toInstant());

    @Aggregation(pipeline = {
            "{ $match : { listingId: ?0, dateBooked: { $gte : ?1, $lt: ?2 }}}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}"
    })
    List<Booking> findBookingsWithinWeek(String listingId, Date beginOfWeek, Date startOfNextWeek);

    // Get current month
    LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();
    Date beginOfMonth = Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant());

    // Get the start of the next month
    LocalDateTime endOfMonth = startOfMonth.plusMonths(1);
    Date startOfNextMonth = Date.from(endOfMonth.atZone(ZoneId.systemDefault()).toInstant());

    @Aggregation(pipeline = {
            "{ $match : { listingId: ?0, dateBooked: { $gte : ?1, $lt: ?2 }}}",
            "{ $sort : { dateBooked: 1, 'bookingDuration.startTime' : 1}}"
    })
    List<Booking> findBookingsWithinMonth(String listingId, Date beginOfMonth, Date startOfNextMonth);

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
