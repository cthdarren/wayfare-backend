package com.wayfare.backend.controller;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

import com.google.maps.errors.ApiException;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import okhttp3.Response;


@RestController
public class BookingController {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    public BookingController(BookingRepository bookingRepository, UserRepository userRepository, TourRepository tourRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
    }


    // GET METHODS

    // get booking based on its id
    @GetMapping("/api/v1/booking/{id}")
    public ResponseObject getBooking(@PathVariable String id){
        Optional<Booking> booking = bookingRepository.findById(id);

        if (!Objects.equals(getCurrentUserDetails().getId(), booking.get().getUserId())){
            return new ResponseObject(false, "You cannot access a booking you do not own!");
        }

        if (booking.isEmpty()){
            return new ResponseObject(false, "Booking not found");
        } else {
            return new ResponseObject(true, booking);
        }
    }

    // get all bookings under a username
    @GetMapping("/api/v1/user/booking/{username}")
    public ResponseObject getUserBooking(@PathVariable String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            return new ResponseObject(false, "Username not found");
        }

        List<Booking> listByUserId = bookingRepository.findAllByUserId(user.getId());

        if (!Objects.equals(getCurrentUserDetails().getId(), user.getId())){
            return new ResponseObject(false, "You cannot access bookings you do not own!");
        }

        return new ResponseObject(true, listByUserId);
    }

    // POST METHODS

    // create booking under LISTING ID
    @PostMapping("/booking/create/{id}")
    public ResponseObject createBooking(@PathVariable String id, @RequestBody BookingDTO dto) {
        Date dateBooked = dto.getDateBooked();
        TimeRange bookingDuration = dto.getBookingDuration();
        List<Booking> conflictingBookings = bookingRepository.findByDateBookedAndBookingDuration(dateBooked, bookingDuration);

        if (!conflictingBookings.isEmpty()) {
            return new ResponseObject(false, "This slot has already been reserved");
        }

        Optional<TourListing> tourListing = tourRepository.findById(id);

        Booking toAdd;
        if (tourListing.isEmpty()) {
            return new ResponseObject(false, "No such listing");
        } else {
            dto.validate();
            if (dto.hasErrors()) {
                return new ResponseObject(false, dto.getErrors());
            }
            WayfareUserDetails user = getCurrentUserDetails();
            toAdd = null;
            try {
                toAdd = new Mapper(tourRepository).toBooking(dto, id);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseObject(false, "Server error");
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new ResponseObject(false, "Server error");
            } catch (ApiException e) {
                e.printStackTrace();
                return new ResponseObject(false, "Server error");
            }
        }

        bookingRepository.save(toAdd);
        return new ResponseObject(true, "Booking added");

    }

    // edit booking using its id
    @PostMapping("/booking/edit/{id}")
    public ResponseObject editBooking(@PathVariable String id, @RequestBody BookingDTO dto) {
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}

        Optional<Booking> booking = bookingRepository.findById(id);


        if (!Objects.equals(getCurrentUserDetails().getId(), booking.get().getUserId())){
            return new ResponseObject(false, "You cannot edit a booking you do not own!");
        }

        Booking bookingToUpdate = booking.get();
        bookingToUpdate.setBookingDuration(dto.getBookingDuration());
        bookingToUpdate.setDateBooked(dto.getDateBooked());
        bookingToUpdate.setBookingPrice(dto.getBookingPrice());
        bookingToUpdate.setPax(dto.getPax());
        bookingToUpdate.setRemarks(dto.getRemarks());
        bookingRepository.save(bookingToUpdate);
        return new ResponseObject(true, "Booking updated");

    }

    // delete booking using its id

    @PostMapping("/booking/delete/{id}")
    public ResponseObject deleteBooking(@PathVariable String id) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (!Objects.equals(getCurrentUserDetails().getId(), booking.get().getUserId())){
            return new ResponseObject(false, "You cannot delete a booking you do not own!");
        }

        bookingRepository.delete(booking.get());
        return new ResponseObject(true, "Booking successfully deleted");


    }



}
