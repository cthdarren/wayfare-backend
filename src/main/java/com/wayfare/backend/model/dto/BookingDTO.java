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
    private BookingStatusEnum status;
    private BookingRepository bookingRepository;
    public BookingDTO(TimeRange bookingDuration, Date dateBooked, Double bookingPrice, int pax, String remarks, BookingStatusEnum status, BookingRepository bookingRepository) {
        this.bookingDuration = bookingDuration;
        this.dateBooked = dateBooked;
        this.bookingPrice = bookingPrice;
        this.pax = pax;
        this.remarks = remarks;
        this.status = status;
        this.bookingRepository = bookingRepository;
    }

    public TimeRange getBookingDuration() {return bookingDuration;}

    public void setBookingDuration(TimeRange bookingDuration) {
        Date dateBooked = getDateBooked();

        if (dateBooked != null && hasBookingConflict(bookingDuration, dateBooked)){

            addErrors("This time slot is reserved for another booking");

        } else {

            this.bookingDuration = bookingDuration;

        }
    }

    private boolean hasBookingConflict(TimeRange bookingDuration, Date dateBooked) {
        List<Booking> conflictingBookings = bookingRepository.findByDateAndTime(dateBooked, bookingDuration);
        return !conflictingBookings.isEmpty();
    }

    public Date getDateBooked() {return dateBooked;}

    public void setDateBooked(Date dateBooked) {this.dateBooked = dateBooked;}

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

    public void setPax(int pax) {this.pax = pax;}

    public String getRemarks() {return remarks;}

    public void setRemarks(String remarks) {this.remarks = remarks;}

    public BookingStatusEnum getStatus() {return status;}

    @Override
    public void validate() {
        if (getBookingDuration() == null || getBookingPrice() == null || getDateBooked() == null || getStatus() == null || getPax() == 0){
            addErrors("Missing JSON Fields");
        }
        getErrors().remove(null);
    }
}
