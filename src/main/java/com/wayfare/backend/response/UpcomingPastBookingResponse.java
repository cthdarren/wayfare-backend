package com.wayfare.backend.response;

import java.util.List;

public class UpcomingPastBookingResponse {

        public List<BookingResponse> upcoming;
        public List<BookingResponseWithReview> past;
        public UpcomingPastBookingResponse(List<BookingResponse> upcoming, List<BookingResponseWithReview> past){
            this.upcoming = upcoming;
            this.past = past;
        }


    }
