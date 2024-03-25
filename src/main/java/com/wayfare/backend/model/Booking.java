package com.wayfare.backend.model;

import com.wayfare.backend.model.object.TimeRange;

import java.time.LocalDate;

public class Booking {
    private String id;
    private LocalDate bookingDate;
    private TimeRange timeSlot;
    private String userId;
    private String listingId;
}