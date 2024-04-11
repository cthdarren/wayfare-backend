package com.wayfare.backend.response;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.BookingStatusEnum;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.object.TimeRange;

import java.time.Instant;
import java.util.Date;

public class BookingResponseWithReview extends BookingResponse {
    public boolean reviewed;
    public BookingResponseWithReview(TourListing listing, String userId, TimeRange bookingDuration, Date startDateTime, Date dateBooked, Double bookingPrice, int pax, String remarks, BookingStatusEnum status, ReturnedUser user, boolean reviewed){
        super(listing, userId, bookingDuration, startDateTime, dateBooked, bookingPrice, pax, remarks,status, user);
        this.reviewed = reviewed;
    }

}

