package com.wayfare.backend.model.dto;

import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.BookingStatusEnum;
import com.wayfare.backend.model.ValidateClass;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.TourRepository;

import java.util.Date;
import java.util.List;

public class BookingDTO extends ValidateClass {

    private TimeRange bookingDuration;
    private Date dateBooked;
    private Double bookingPrice;
    private int pax;
    private String remarks;
    public BookingDTO(TimeRange bookingDuration, Date dateBooked, Double bookingPrice, int pax, String remarks) {
        this.bookingDuration = bookingDuration;
        this.dateBooked = dateBooked;
        this.bookingPrice = bookingPrice;
        this.pax = pax;
        this.remarks = remarks;
    }

    public TimeRange getBookingDuration() {return bookingDuration;}

    public Date getDateBooked() {return dateBooked;}

    public Double getBookingPrice() {return bookingPrice;}

    public void setBookingPrice(Double bookingPrice) {
        if (bookingPrice == null){
            addErrors("Price cannot be null");
        }
        else if(bookingPrice <= 0){
            addErrors("Price must be more than 0");
        }
        else{
            this.bookingPrice = bookingPrice;
        }
    }

    public int getPax() {return pax;}

    public String getRemarks() {return remarks;}

    @Override
    public void validate() {
        if (getBookingDuration() == null || getBookingPrice() == null || getDateBooked() == null || getPax() == 0){
            addErrors("Missing JSON Fields");
        }
        getErrors().remove(null);
    }
}
