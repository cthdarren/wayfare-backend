package com.wayfare.backend.controller;

import com.google.maps.errors.ApiException;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Shorts;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.dto.ShortsDTO;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.ShortsRepository;
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
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class ShortsController {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    private final ShortsRepository shortsRepository;

    public ShortsController(BookingRepository bookingRepository, UserRepository userRepository, TourRepository tourRepository, ShortsRepository shortsRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.shortsRepository = shortsRepository;
    }
    @GetMapping("/api/v1/shorts")
    public ResponseObject getAllShorts() {
        List<Shorts> allShorts = shortsRepository.findAll();
        return new ResponseObject(true, allShorts);
    }
    @PostMapping("/shorts/create")
    public ResponseObject createShortsNoList(@RequestBody ShortsDTO dto) {
        Shorts toAdd;
        dto.validate();
        if (dto.hasErrors()) {
            return new ResponseObject(false, dto.getErrors());
        }
        toAdd = null;
        try {
            toAdd = new Mapper().toShortsNoTour(dto);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseObject(false, "Server error");
        } catch (InterruptedException e) {
            return new ResponseObject(false, "Server error");
        } catch (ApiException e) {
            return new ResponseObject(false, "Server error");
        }
        shortsRepository.save(toAdd);
        return new ResponseObject(true, "Short added");
    }
    @PostMapping("/shorts/create/{id}")
    public ResponseObject createBooking(@PathVariable String id, @RequestBody ShortsDTO dto) {
        Optional<TourListing> tourListing = tourRepository.findById(id);
        Shorts toAdd;
        if (tourListing.isEmpty()) {
            return new ResponseObject(false, "No such listing");
        }
        dto.validate();
        if (dto.hasErrors()) {
            return new ResponseObject(false, dto.getErrors());
        }
        toAdd = null;
        try {
            toAdd = new Mapper(tourRepository).toShorts(dto,id);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseObject(false, "Server error");
        } catch (InterruptedException e) {
            return new ResponseObject(false, "Server error");
        } catch (ApiException e) {
            return new ResponseObject(false, "Server error");
        }
        shortsRepository.save(toAdd);
        return new ResponseObject(true, "Short added");
    }
    @PostMapping("/shorts/liked/{id}")
    public ResponseObject shortsLiked(@PathVariable String id) {
        WayfareUserDetails user = getCurrentUserDetails();
        Optional<Shorts> shortsData = shortsRepository.findById(id);
        if (shortsData.isEmpty()) {
            return new ResponseObject(false, "No such shorts");
        }else{
            shortsData.ifPresent(shorts -> shorts.addLike(user.getId()));
        }
        return new ResponseObject(true, "Liked");
    }
    @PostMapping("/shorts/unliked/{id}")
    public ResponseObject shortsUnliked(@PathVariable String id) {
        WayfareUserDetails user = getCurrentUserDetails();
        Optional<Shorts> shortsData = shortsRepository.findById(id);
        if (shortsData.isEmpty()) {
            return new ResponseObject(false, "No such shorts");
        }else{
            shortsData.ifPresent(shorts -> shorts.removeLike(user.getId()));
        }
        return new ResponseObject(true, "Unliked");
    }
}

