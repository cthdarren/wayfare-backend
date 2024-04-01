package com.wayfare.backend.response;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.BookingStatusEnum;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.object.TimeRange;

import java.util.Date;

public class BookingResponse extends Booking {
    public ReturnedUser user;
    public BookingResponse(TourListing listing, String userId, TimeRange bookingDuration, Date dateBooked, Double bookingPrice, int pax, String remarks, BookingStatusEnum status, ReturnedUser user){
        super(listing, userId, bookingDuration, dateBooked, bookingPrice, pax, remarks,status);
        this.user = user;
    }

}

