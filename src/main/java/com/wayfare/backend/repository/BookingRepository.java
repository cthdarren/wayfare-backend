package com.wayfare.backend.repository;

import com.wayfare.backend.model.Booking;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {

    List<Booking> findAllByBookingId(String bookingId);
    List<Booking> findAllByListingId(String listingId);
    List<Booking> findAllByUserId(String userId);
    List<Booking> findAllByWayFarerId(String wayFarerId);

    // aggregation pipeline date booked --> time slot

}
