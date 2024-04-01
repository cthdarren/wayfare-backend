package com.wayfare.backend.model;

import com.wayfare.backend.model.object.TimeRange;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document(collection = "bookings")
public class Booking {

    private TourListing listing;
    private String userId;
    private TimeRange bookingDuration;
    private Date dateBooked;
    private Double bookingPrice;
    private int pax;
    private String remarks;
    private BookingStatusEnum status;

    public Booking (){}

    public Booking(TourListing listing, String userId, TimeRange bookingDuration, Date dateBooked, Double bookingPrice, int pax, String remarks, BookingStatusEnum status) {
        this.listing = listing;
        this.userId = userId;
        this.bookingDuration = bookingDuration;
        this.dateBooked = dateBooked;
        this.bookingPrice = bookingPrice;
        this.pax = pax;
        this.remarks = remarks;
        this.status = status;
    }

    public TourListing getListingId() {
        return listing;
    }

    public String getUserId() {
        return userId;
    }

    public TimeRange getBookingDuration() {
        return bookingDuration;
    }

    public void setBookingDuration(TimeRange bookingDuration) {
        this.bookingDuration = bookingDuration;
    }

    public Date getDateBooked() {
        return dateBooked;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public Double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(Double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    public int getPax() {
        return pax;
    }

    public void setPax(int pax) {
        this.pax = pax;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BookingStatusEnum getStatus() {
        return status;
    }

    public void setStatus(BookingStatusEnum status) {
        this.status = status;
    }
}
