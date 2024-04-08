package com.wayfare.backend.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.errors.ApiException;
import com.mongodb.MongoQueryException;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.LocationRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.security.WayfareUserDetails;
import com.wayfare.backend.helper.Mapper;

import org.apache.coyote.Response;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class TourController {
    private final UserRepository userRepo;
    private final TourRepository tourRepo;
    private final BookingRepository bookingRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public TourController(UserRepository userRepo, TourRepository tourRepo, BookingRepository bookingRepo) {
        this.userRepo = userRepo;
        this.tourRepo = tourRepo;
        this.bookingRepo = bookingRepo;
    }

    // GET METHODS

    // get all dates booked
    @GetMapping("/api/v1/listing/{id}/bookings")
    public ResponseObject getDatesBooked(@PathVariable String id){
        List<Date> bookedDates = bookingRepo.findBookedDatesByListingId(id);
        return new ResponseObject(true, bookedDates);
    }


    @GetMapping("/api/v1/listing/{id}")
    public ResponseObject getTourListing(@PathVariable String id) {
        Optional<TourListing> tourListing = tourRepo.findById(id);
        if (tourListing.isEmpty()) {
            return new ResponseObject(false, "Listing not found");
        } else {
            return new ResponseObject(true, tourListing);
        }
    }

    @GetMapping("/api/v1/listing/search")
    public ResponseObject getListingsByLocation
            (
            @RequestParam(required = false) String longitude,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String kmdistance,
            @RequestParam(required = false) String numberPax,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
            )
    {

        try {
            List<TourListing> combinedListings = new ArrayList<>();

            // Check if location parameters are provided
            if (longitude != null && latitude != null && kmdistance != null && numberPax != null) {
                double dLong = Double.parseDouble(longitude);
                double dLat = Double.parseDouble(latitude);
                double dDist = Double.parseDouble(kmdistance);
                Integer iNumberPax = Integer.parseInt(numberPax);
                if (dLong < -180 || dLong > 180 || dLat < -90 || dLat > 90) {
                    return new ResponseObject(false, "Invalid coordinates");
                }

                // Perform the location-based search
                List<TourListing> listByLocation = tourRepo.findByLocationNearAndMaxPaxGreaterThanEqualAndMinPaxLessThanEqualOrderByRatingDesc(
                        new Point(dLong,dLat),
                        new Distance(dDist, Metrics.KILOMETERS),
                        iNumberPax,
                        iNumberPax
                );

                combinedListings.addAll(listByLocation);
            }

            // Check if date range parameters are provided
            if (startDate != null && endDate != null) {
                // Perform the date range search
                List<TourListing> availableListings = tourRepo.findAvailableListingsByDateRange(startDate, endDate);

                if (combinedListings.isEmpty()) {
                    combinedListings.addAll(availableListings);
                } else {
                    // Retains listings matching both criteria
                    combinedListings.retainAll(availableListings);
                }
            }
            return new ResponseObject(true, combinedListings);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return new ResponseObject(false, "Invalid parameters");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseObject(false, "Error fetching listings");
        }

    }

    @GetMapping("/api/v1/user/listing/{username}")
    public ResponseObject getListingsByUserId(@PathVariable String username) {
        User user = userRepo.findByUsername(username);
        if (user == null){
            return new ResponseObject(false, "Username not found");
        }
        List<TourListing> listByUserId = tourRepo.findAllByUserId(user.getId());
        return new ResponseObject(true, listByUserId);
    }



    //POST METHODS
    @PostMapping("/wayfarer/listing/create")
    public ResponseObject createTourListing(@RequestBody TourListingDTO dto) {
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}
        WayfareUserDetails user = getCurrentUserDetails();
        TourListing toAdd = null;
        try {
            toAdd = new Mapper().toTourListing(dto, user.getId());
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

        tourRepo.save(toAdd);
        return new ResponseObject(true, "Listing added");
    }


    // Delete Listing
    @PostMapping("/wayfarer/listing/delete/{id}")
    public ResponseObject deleteListing(@PathVariable String id) {
        Optional<TourListing> tourListing = tourRepo.findById(id);

        if (tourListing.isEmpty()){
            return new ResponseObject(false, "No such listing");
        }

        if (!Objects.equals(getCurrentUserDetails().getId(), tourListing.get().getUserId())){
            return new ResponseObject(false, "You cannot delete a listing that you do not own!");
        }

        tourRepo.delete(tourListing.get());
        return new ResponseObject(true, "Listing successfully deleted");

    }


    // Edit Listing

    @PostMapping("/wayfarer/listing/edit/{id}")
    public ResponseObject editListing(@PathVariable String id, @RequestBody TourListingDTO dto) {
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}

        Optional<TourListing> tourListing = tourRepo.findById(id);

        if (tourListing.isEmpty()){
            return new ResponseObject(false, "No such listing");
        }

        if (!Objects.equals(getCurrentUserDetails().getId(), tourListing.get().getUserId())){
            return new ResponseObject(false, "You cannot edit a listing that you do not own!");
        }

        TourListing listingToUpdate = tourListing.get();
        listingToUpdate.setTitle(dto.getTitle());
        listingToUpdate.setDescription(dto.getDescription());
        listingToUpdate.setLocation(dto.getLocation());
        listingToUpdate.setTimeRangeList(dto.getTimeRangeList());
        listingToUpdate.setPrice(dto.getPrice());
        listingToUpdate.setMinPax(dto.getMinPax());
        listingToUpdate.setMaxPax(dto.getMaxPax());
        listingToUpdate.setThumbnailUrls(dto.getThumbnailUrls());
        listingToUpdate.setCategory(dto.getCategory());

        //save editing
        tourRepo.save(listingToUpdate);
        return new ResponseObject(true, "Listing updated");

    }



}