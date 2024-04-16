package com.wayfare.backend.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.maps.errors.ApiException;
import com.mongodb.MongoQueryException;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.object.TimeRange;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.ReviewRepository;
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
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class TourController {
    private final UserRepository userRepo;
    private final TourRepository tourRepo;
    private final BookingRepository bookingRepo;
    private final ReviewRepository reviewRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public TourController(UserRepository userRepo, TourRepository tourRepo, BookingRepository bookingRepo, ReviewRepository reviewRepo) {
        this.userRepo = userRepo;
        this.tourRepo = tourRepo;
        this.bookingRepo = bookingRepo;
        this.reviewRepo = reviewRepo;
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
    public ResponseObject getListingsByLocationAndDate
            (
                    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(required = false) String longitude,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String kmdistance,
            @RequestParam(required = false) String numberPax,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
            )
    {
        if (longitude == null & latitude == null & kmdistance == null & numberPax == null & startDate == null & endDate == null)
        {
            return new ResponseObject(true,  tourRepo.findByLocationNear(new Point(0, 0), new Distance(20050.0, Metrics.KILOMETERS)));
        }
        if (longitude != null & latitude != null & kmdistance != null){
            Double doubleLong = Double.parseDouble(longitude);
            Double doubleLat = Double.parseDouble(latitude);
            Double doubleKmdistance = Double.parseDouble(kmdistance);
            if (numberPax == null & startDate == null & endDate == null){
                return new ResponseObject(true, tourRepo.findByLocationNear(
                        new Point(doubleLong, doubleLat),
                        new Distance(doubleKmdistance, Metrics.KILOMETERS)));
            }
            if (numberPax != null){
                Integer intPax = Integer.parseInt(numberPax);
                if (startDate == null & endDate == null){
                    return new ResponseObject(true, tourRepo.findByLocationNearAndMaxPaxGreaterThanEqualAndMinPaxLessThanEqual(
                            new Point(doubleLong, doubleLat),
                            new Distance(doubleKmdistance, Metrics.KILOMETERS),
                            intPax,
                            intPax
                    ));
                }
                else if (startDate != null & endDate != null){
                    return new ResponseObject(true, tourRepo.findWithAllParams(doubleLong, doubleLat, new Distance(doubleKmdistance, Metrics.KILOMETERS).getNormalizedValue(), startDate, endDate));
                }
            }
            if (startDate != null & endDate != null){
                return new ResponseObject(true, tourRepo.findAvailableListingsByLocAndDateRange(doubleLong, doubleLat, new Distance(doubleKmdistance, Metrics.KILOMETERS).getNormalizedValue(), startDate, endDate));
            }
            else
                return new ResponseObject(false, "Invalid parameters");
        }
        if (numberPax != null){
            Integer intPax = Integer.parseInt(numberPax);
            if (startDate != null & endDate != null){
                return new ResponseObject(true, tourRepo.findAvailableListingsByPaxAndDateRange(intPax, startDate, endDate));
            }
            return new ResponseObject(true, tourRepo.findByMaxPaxGreaterThanEqualAndMinPaxLessThanEqual(intPax, intPax));
        }
        if (startDate != null & endDate != null){
            return new ResponseObject(true, tourRepo.findAvailableListingsByDateRange(startDate, endDate));
        }
        return new ResponseObject(false, "Invalid parameters");
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
            System.out.println(toAdd.getDateCreated());
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

        if (bookingRepo.existsByListingId(id))
            return new ResponseObject(false, "You must cancel any ongoing bookings before deleting this listing");

        if (!Objects.equals(getCurrentUserDetails().getId(), tourListing.get().getUserId())){
            return new ResponseObject(false, "You cannot delete a listing that you do not own!");
        }

        tourRepo.delete(tourListing.get());
        int reviewsDeleted = reviewRepo.deleteAllByListingId(id);
        return new ResponseObject(true, String.format("Listing successfully deleted along with %d reviews", reviewsDeleted));

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