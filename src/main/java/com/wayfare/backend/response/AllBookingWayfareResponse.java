package com.wayfare.backend.response;

import java.util.List;

public class AllBookingWayfareResponse {

    public List<BookingResponse> day;
    public List<BookingResponse> week;
    public List<BookingResponse> month;
    public AllBookingWayfareResponse(List<BookingResponse> day, List<BookingResponse> week, List<BookingResponse> month) {
        this.day = day;
        this.week = week;
        this.month = month;
    }
}
