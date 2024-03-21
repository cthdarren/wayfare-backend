package com.wayfare.backend.controller;

import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.ReviewsByListingRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/v1/review/{id}")
    public ResponseObject getReview(@PathVariable String id) {
        Optional<Review> review = reviewRepo.findById(id);
        if (review.isEmpty()) {
            return new ResponseObject(false, "Review not found");
        } else {
            return new ResponseObject(true, review);
        }
    }

    @PostMapping("/api/v1/listing/reviews")
    public ResponseObject getReviewsByListing(@RequestBody ReviewsByListingRequest request){
        List<Review> reviewList = reviewRepo.findAllByListingId(request.listingId());

        return new ResponseObject(true, reviewList);
    }

    @PostMapping("/api/v1/listing/newest-five-reviews")
    public ResponseObject getFirstFiveReviewsByListing(@RequestBody ReviewsByListingRequest request){
        List<Review> reviewList = reviewRepo.findFirst5ByListingIdOrderByDateCreatedDesc(request.listingId());

        return new ResponseObject(true, reviewList);
    }

    // MUST BE AUTHORISED AS USER
    // *** GET ***
    // Gets all the reviews created by the logged-in user.
    // Authenticates using Bearer token in headers.
    @GetMapping("/reviews")
    public ResponseObject getReviewsByUser(){
        WayfareUserDetails user = getCurrentUserDetails();
        List<Review> reviewList = reviewRepo.findAllByUserId(user.getId());
        return new ResponseObject(true, reviewList);
    }

    /// MUST BE AUTHORISED AS USER
    // *** POST ***
    // Gets all the reviews created by the logged-in user.
    // Authenticates using Bearer token in headers.

    // Accepts application/json
    // {"title": TITLE, "score":INTEGER SCORE, "reviewContent" CONTENT, "listingId": LISTINGID}
    @PostMapping("/review/create")
    public ResponseObject createReview(@RequestBody ReviewDTO dto){
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}
        WayfareUserDetails user = getCurrentUserDetails();
        Review toAdd = new Mapper().toReview(dto, user.getId());

        if(reviewRepo.existsByUserIdAndListingId(user.getId(), dto.getListingId()))
            return new ResponseObject(false, "Review already exists");
        reviewRepo.save(toAdd);
        return new ResponseObject(true, "Review added");
    }
}

