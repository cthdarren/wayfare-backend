package com.wayfare.backend.controller;

import com.google.maps.errors.ApiException;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Comment;
import com.wayfare.backend.model.CommentWithUser;
import com.wayfare.backend.model.Shorts;
import com.wayfare.backend.model.ShortsWithComment;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.dto.ShortsDTO;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.CommentRepository;
import com.wayfare.backend.repository.ShortsRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.CommentRequest;
import com.wayfare.backend.response.ResponseObject;

import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class ShortsController {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final CommentRepository commentRepo;
    private final ShortsRepository shortsRepository;

    public ShortsController(BookingRepository bookingRepository, UserRepository userRepository, TourRepository tourRepository, ShortsRepository shortsRepository, CommentRepository commentRepo) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.commentRepo = commentRepo;
        this.shortsRepository = shortsRepository;
    }
    @GetMapping("/api/v1/shorts")
    public ResponseObject getAllShorts() {
        List<ShortsWithComment> result = new ArrayList<>();
        List<Shorts> allShorts = shortsRepository.findAll();
        for (Shorts sh: allShorts){
            List<CommentWithUser> comments = commentRepo.findAllJoinUserByJourneyId(sh.getId());
            result.add(new ShortsWithComment(sh.getShortsUrl(), sh.getUserName(), sh.getUserId(), sh.getDescription(), sh.getDatePosted(), sh.getListing(), sh.getLikes(), sh.getThumbnailUrl(), comments));
        }
        return new ResponseObject(true, result);
    }

    @GetMapping("/api/v1/short/{id}")
    public ResponseObject getShortById(@PathVariable String id){
        Optional<Shorts> toView = shortsRepository.findById(id);
        if (toView.isEmpty())
            return new ResponseObject(false, "Journey not found");
        
        return new ResponseObject(true, toView);
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseObject(false, "Listing not found");
        }
        shortsRepository.save(toAdd);
        return new ResponseObject(true, "Journey added");
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseObject(false, "Listing not found");
        }
        shortsRepository.save(toAdd);
        return new ResponseObject(true, "Journey added");
    }
    @PostMapping("/shorts/liked/{id}")
    public ResponseObject shortsLiked(@PathVariable String id) {
        WayfareUserDetails user = getCurrentUserDetails();
        Shorts shortsData = shortsRepository.findById(id).orElseThrow();
        shortsData.addLike(user.getUsername());
        shortsRepository.save(shortsData);
        return new ResponseObject(true, "Liked");
    }
    @PostMapping("/shorts/unliked/{id}")
    public ResponseObject shortsUnliked(@PathVariable String id) {
        WayfareUserDetails user = getCurrentUserDetails();
        Shorts shortsData = shortsRepository.findById(id).orElseThrow();
        shortsData.removeLike(user.getUsername());
        shortsRepository.save(shortsData);
        return new ResponseObject(true, "Unliked");
    }

    @PostMapping("/shorts/comment")
    public ResponseObject commentOnJourney(@RequestBody CommentRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        if (shortsRepository.existsById(request.journeyId())){
            Comment toAdd = new Comment(request.journeyId(),user.getId(), request.comment(), Instant.now());
            commentRepo.save(toAdd);
            return new ResponseObject(true, "Comment created");
        }
        else
            return new ResponseObject(false, "Journey not found");
    }
}

