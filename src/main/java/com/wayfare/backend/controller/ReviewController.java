package com.wayfare.backend.controller;

import com.wayfare.backend.model.Review;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.CreateReviewRequest;
import com.wayfare.backend.request.ReviewsByListingRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class ReviewController {
    private final ReviewRepository reviewRepo;

    private final UserRepository userRepo;

    public ReviewController(ReviewRepository reviewRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/api/review/{id}")
    public ResponseObject getReview(@PathVariable String id) {
        Optional<Review> review = reviewRepo.findById(id);
        if (review.isEmpty()) {
            return new ResponseObject(false, "Review not found");
        } else {
            return new ResponseObject(true, review);
        }
    }

    @PostMapping("/api/listing/reviews")
    public ResponseObject getReviewsByListing(@RequestBody ReviewsByListingRequest request){
        List<Review> reviewList = reviewRepo.findAllByListingId(request.listingId());

        return new ResponseObject(true, reviewList);
    }

    // MUST BE AUTHORISED AS USER
    @GetMapping("/reviews")
    public ResponseObject getReviewsByUser(){
        WayfareUserDetails user = getCurrentUserDetails();
        List<Review> reviewList = reviewRepo.findAllByUserId(user.getId());
        return new ResponseObject(true, reviewList);
    }

    // MUST BE AUTHORISED AS USER
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
        return new ResponseObject(true, "Review added");
    }
}

