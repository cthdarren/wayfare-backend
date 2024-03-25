package com.wayfare.backend.controller;

import java.util.Optional;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.request.LocationRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.security.WayfareUserDetails;
import com.wayfare.backend.helper.Mapper;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class TourController {
    private final TourRepository tourRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public TourController(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }

    // GET METHODS
    @GetMapping("/api/v1/listing/{id}")
    public ResponseObject getTourListing(@PathVariable String id) {
        Optional<TourListing> tourListing = tourRepo.findById(id);
        if (tourListing.isEmpty()) {
            return new ResponseObject(false, "Listing not found");
        } else {
            return new ResponseObject(true, tourListing);
        }
    }

    @PostMapping("/api/v1/listing/search")
    public ResponseObject getListingsByLocation(@RequestBody LocationRequest request) {
        List<TourListing> listByCountry = tourRepo.findByLocationNearOrderByRating(new Point(request.longitude(), request.latitude()), new Distance(request.kmdistance(), Metrics.KILOMETERS));
        return new ResponseObject(true, listByCountry);
    }

//    @GetMapping("/api/v1/listing/user")
//    public ResponseObject getListingsByUserId(@RequestBody TourListingsByUser request) {
//        List<TourListing> listByUserId = tourRepo.findAllByUserId(request.userId());
//        return new ResponseObject(true, listByUserId);
//    }

    //POST METHODS
    @PostMapping("/listing/create")
    public ResponseObject createTourListing(@RequestBody TourListingDTO dto) {
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}
        WayfareUserDetails user = getCurrentUserDetails();
        TourListing toAdd = new Mapper().toTourListing(dto, user.getId());

        tourRepo.save(toAdd);
        return new ResponseObject(true, "Listing added");
    }
}