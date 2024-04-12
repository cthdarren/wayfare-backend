package com.wayfare.backend.controller;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

import com.google.maps.errors.ApiException;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.response.*;
import com.wayfare.backend.security.WayfareUserDetails;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.String;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private final ReviewRepository reviewRepo;

    public BookingController(BookingRepository bookingRepository, UserRepository userRepository, TourRepository tourRepository, ReviewRepository reviewRepo) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.reviewRepo = reviewRepo;
    }


    // GET METHODS

    // get booking based on its id
    @GetMapping("/booking/{id}")
    public ResponseObject getBooking(@PathVariable String id){
        BookingResponse booking = bookingRepository.findBooking(id);
        WayfareUserDetails curr = getCurrentUserDetails();

        if (booking == null)
            return new ResponseObject(false, "Booking not found");

        if (!(Objects.equals(curr.getId(), booking.getUserId()) | Objects.equals(curr.getId(), booking.getListing().getUserId()))){
            return new ResponseObject(false, "You cannot access a booking you do not own!");
        }

        return new ResponseObject(true, booking);
    }

    @GetMapping("/wayfarer/booking/{id}")
    public ResponseObject getBookingWayfarer(@PathVariable String id){
        BookingResponse booking = bookingRepository.findBookingAsWayfarer(id);
        WayfareUserDetails curr = getCurrentUserDetails();

        if (booking == null)
            return new ResponseObject(false, "Booking not found");

        if (!(Objects.equals(curr.getId(), booking.getUserId()) | Objects.equals(curr.getId(), booking.getListing().getUserId()))){
            return new ResponseObject(false, "You cannot access a booking you do not own!");
        }

        return new ResponseObject(true, booking);
    }

    // get all bookings under a username
    @GetMapping("/bookings")
    public ResponseObject getUserBooking(){

        WayfareUserDetails user = getCurrentUserDetails();
        if (user == null){
            return new ResponseObject(false, "Username not found");
        }

        List<BookingResponse> upcomingBookings = bookingRepository.findAllUpcomingBookings(user.getId());
        List<BookingResponseWithReview> pastBookings = bookingRepository.findAllPastBookings(user.getId());

        return new ResponseObject(true, new UpcomingPastBookingResponse(upcomingBookings, pastBookings));
    }

    // get all bookings as a wayfarer
    @GetMapping("/wayfarer/bookings")
    public ResponseObject getWayfarerBooking(){

        WayfareUserDetails user = getCurrentUserDetails();

        // Get tour listings for the user
        List<TourListing> userTourListings = tourRepository.findAllByUserId(user.getId());

        // Collect the listing IDs
        List<String> listingIds = new ArrayList<>();
        for (TourListing listing : userTourListings) {
            listingIds.add(listing.getId());
        }

        // Get the current date from now
        Date beginOfDay = Date.from(Instant.now());

        // Until 24 hours from now
        Instant startOfNext= Instant.now().plus(1, ChronoUnit.DAYS);
        Date startOfNextDay = Date.from(startOfNext);

        List<BookingResponse> bookingsForTheDay = bookingRepository.findBookingsWithinDay(listingIds, beginOfDay, startOfNextDay);

        // Get the end period of the previous date
        Date beginOfWeek = Date.from(startOfNext);

        // Add one week
        Instant endWeek = startOfNext.plus(7, ChronoUnit.DAYS);
        Date startOfNextWeek = Date.from(endWeek);

        List<BookingResponse> bookingsForTheWeek = bookingRepository.findBookingsWithinWeek(listingIds, beginOfDay, startOfNextWeek);

        // Get end period of the previous week query
        Date beginOfMonth = Date.from(endWeek);

        //Find all remaining upcoming bookings
        List<BookingResponse> bookingsForTheMonth = bookingRepository.findRestOfBookings(listingIds, beginOfDay);

        return new ResponseObject(true, new AllBookingWayfareResponse(bookingsForTheDay, bookingsForTheWeek, bookingsForTheMonth));
    }

    // Wayfarer past bookings
    @GetMapping("/wayfarer/pastbookings")
    public ResponseObject getPastBookingsAsWayfarer(){

        WayfareUserDetails user = getCurrentUserDetails();
        if (user == null){
            return new ResponseObject(false, "Username not found");
        }

        List<BookingResponseWithReview> pastBookings = bookingRepository.findAllPastBookingsWithWayfarerId(user.getId());

        return new ResponseObject(true, pastBookings);
    }


    // create booking under LISTING ID
    @PostMapping("/booking/create")
    public ResponseObject createBooking(@RequestBody BookingDTO dto) {
        Date dateBooked = dto.getDateBooked();
        TimeRange bookingDuration = dto.getBookingDuration();
        List<Booking> conflictingBookings = bookingRepository.findByListingIdAndDateBookedAndBookingDuration(dto.getListingId(), dateBooked, bookingDuration);

        if (!conflictingBookings.isEmpty()) {
            return new ResponseObject(false, "This slot has already been reserved");
        }

        Optional<TourListing> tourListing = tourRepository.findById(dto.getListingId());

        boolean paxVal = dto.getPax() >= tourListing.get().getMinPax() && dto.getPax() <= tourListing.get().getMaxPax();

        if (!paxVal) {
            return new ResponseObject(false, "The pax specified does not fall in the listing's range");
        }

        boolean isValidBooking = false;
        for (TimeRange range : tourListing.get().getTimeRangeList()) {
            if (dto.getBookingDuration().getStartTime() >= range.getStartTime() &&
                    dto.getBookingDuration().getEndTime() <= range.getEndTime()) {
                isValidBooking = true;
                break;
            }
        }

        if (!isValidBooking) {
            return new ResponseObject(false, "The specified booking duration is not within the available time ranges.");
        }



        Booking toAdd;
        if (tourListing.isEmpty()) {
            return new ResponseObject(false, "No such listing");
        } else {
            dto.validate();
            if (dto.hasErrors()) {
                return new ResponseObject(false, dto.getErrors());
            }
            toAdd = null;
            try {
                toAdd = new Mapper(tourRepository).toBooking(dto);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseObject(false, "Server error");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ApiException e) {
                throw new RuntimeException(e);
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

        Date dateBooked = dto.getDateBooked();
        TimeRange bookingDuration = dto.getBookingDuration();
        List<Booking> conflictingBookings = bookingRepository.findByListingIdAndDateBookedAndBookingDuration(dto.getListingId(), dateBooked, bookingDuration);

        if (!conflictingBookings.isEmpty()) {
            return new ResponseObject(false, "This slot has already been reserved");
        }

        if (booking.isEmpty()){
            return new ResponseObject(false, "Booking does not exist");
        }

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
        if (booking.isEmpty()){
            return new ResponseObject(false, "Booking does not exist");
        }

        Booking bookingFound = booking.get();
        String currId = getCurrentUserDetails().getId();

        if (!Objects.equals(currId, bookingFound.getUserId()) & !Objects.equals(currId, bookingFound.getListing().getUserId())){
            return new ResponseObject(false, "You cannot delete a booking you do not own!");
        }

        bookingRepository.delete(bookingFound);
        return new ResponseObject(true, "Booking successfully deleted");


    }



}
