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
            "{ $match : { dateBooked : ?0}}",
            "{ $match : {$expr : { $and : [{$gte : [$bookingDuration.startTime, ?1.startTime]}, {$lte : [bookingDuration.endTime, ?1.endTime]}]}}}"
    })
    List<Booking> findByDateAndTime(Date dateBooked, TimeRange bookingDuration);
}
