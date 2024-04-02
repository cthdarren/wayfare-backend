package com.wayfare.backend.response;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.BookingStatusEnum;
import com.wayfare.backend.model.Bookmark;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.object.TimeRange;

import java.util.Date;

public class BookmarkResponse extends Bookmark {
    public ReturnedUser user;
    public BookmarkResponse(TourListing listing, String userId, ReturnedUser user){
        super(listing, userId);
        this.user = user;
    }

}

