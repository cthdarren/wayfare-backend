package com.wayfare.backend.response;

import java.util.List;

public class UpcomingPastBookingResponse {

        public List<BookingResponse> upcoming;
        public List<BookingResponse> past;
        public UpcomingPastBookingResponse(List<BookingResponse> upcoming, List<BookingResponse> past){
            this.upcoming = upcoming;
            this.past = past;
        }


    }
