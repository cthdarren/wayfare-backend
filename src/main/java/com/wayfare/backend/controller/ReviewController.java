package com.wayfare.backend.controller;

import com.wayfare.backend.model.Review;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.CreateReviewRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewRepository reviewRepo;

    private final UserRepository userRepo;

    public ReviewController(ReviewRepository reviewRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/{id}")
    public ResponseObject getReview(@PathVariable String id) {
        Optional<Review> review = reviewRepo.findById(id);
        if (review.isEmpty()) {
            return new ResponseObject(false, "review not found!")
        } else {
            return new ResponseObject(true, review);
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseObject getReviewsByListing(@PathVariable String listingId){
        List<Review> reviewList = reviewRepo.findAllByListingId(listingId);
        return new ResponseObject(true, reviewList);
    }

    @GetMapping("/")
    public ResponseObject getReviewsByUser(){
        WayfareUserDetails user = getCurrentUserDetails();
        List<Review> reviewList = reviewRepo.findAllByUserId(user.getId());
        return new ResponseObject(true, reviewList);
    }

    @PostMapping("/create")
    public ResponseObject createReview(@RequestBody CreateReviewRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        Review toAdd = new Review(
                request.title(),
                request.score(),
                request.reviewContent(),
                user.getId(),
                request.listingId()
        );
        reviewRepo.save(toAdd);
        return new ResponseObject(true, "review added");
    }
}

