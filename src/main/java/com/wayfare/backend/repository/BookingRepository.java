package com.wayfare.backend.repository;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.object.TimeRange;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findAllByListingId(String listingId);
    Booking findByListingId(String listingId);
    List<Booking> findAllByUserId(String userId);
    @Aggregation(pipeline = {
            "{ $match : { userId: ?0} }",
            "{ $sort : { dateBooked: -1, \"bookingDuration.startTime\" : 1}}",
    })
    List<Booking> findAllUpcomingBookings(String userId);

    List<Booking> findByDateBookedAndBookingDuration(Date dateBooked, TimeRange bookingDuration);
}
