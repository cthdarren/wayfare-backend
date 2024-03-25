package com.wayfare.backend.controller;

import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.DeleteReviewRequest;
import com.wayfare.backend.request.ReviewsByListingRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;
import static java.lang.Math.round;

@RestController
public class ReviewController {
    private final ReviewRepository reviewRepo;

    private final TourRepository tourRepo;
    private final UserRepository userRepo;

    public ReviewController(ReviewRepository reviewRepo, TourRepository tourRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.tourRepo = tourRepo;
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
        String listingId = dto.getListingId();
        TourListing listing = tourRepo.findById(dto.getListingId()).orElseThrow();

        if(Objects.equals(user.getId(), listing.getUserId()))
            return new ResponseObject(false, "You cannot leave a review on your own listing");

        if(!tourRepo.existsById(listingId))
            return new ResponseObject(false, "Listing does not exist");

        if(reviewRepo.existsByUserIdAndListingId(user.getId(), dto.getListingId()))
            return new ResponseObject(false, "Review already exists");

        reviewRepo.save(toAdd);

        updateListingRatings(listing);

        return new ResponseObject(true, "Review added");
    }

    @PostMapping("review/edit")
    public ResponseObject editReview(@RequestBody ReviewDTO dto){
        WayfareUserDetails user = getCurrentUserDetails();
        Review toEdit = reviewRepo.findByUserIdAndListingId(user.getId(), dto.getListingId());
        if (toEdit == null)
            return new ResponseObject(false, "Review does not exist");
        toEdit.setTitle(dto.getTitle());
        toEdit.setReviewContent(dto.getReviewContent());
        toEdit.setScore(dto.getScore());
        toEdit.setDateModified(Instant.now());
        reviewRepo.save(toEdit);

        TourListing listing = tourRepo.findById(dto.getListingId()).orElseThrow();
        updateListingRatings(listing);

        return new ResponseObject(true, "Successfully edited review");
    }

    @PostMapping("review/delete")
    public ResponseObject deleteReview(@RequestBody DeleteReviewRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        Review toDelete = reviewRepo.findById(request.reviewId()).orElseThrow();

        TourListing listing = tourRepo.findById(toDelete.getListingId()).orElseThrow();

        if (!Objects.equals(toDelete.getUserId(), user.getId()))
            return new ResponseObject(false, "User does not own review");

        reviewRepo.deleteById(request.reviewId());
        updateListingRatings(listing);
        return new ResponseObject(true, "Successfully deleted review");
    }

    public void updateListingRatings(TourListing listing){
        String listingId = listing.getId();
        List<Review> reviewList = reviewRepo.findAllByListingId(listingId);
        // Updating ratings on the specified listing
        Double sum =0.0;
        double average;
        Integer count = 0;
        for (Review review : reviewList) {
            sum += review.getScore();
            count++;
        }
        average = sum/count;
        average = Math.round(average*100);

        listing.setReviewCount(count);
        listing.setRating(average/100.0);
        tourRepo.save(listing);
    }
}

