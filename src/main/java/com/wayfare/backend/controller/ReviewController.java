package com.wayfare.backend.controller;

import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Booking;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.DeleteReviewRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class ReviewController {
    private final ReviewRepository reviewRepo;

    private final TourRepository tourRepo;
    private final UserRepository userRepo;
    private final BookingRepository bookingRepo;

    public ReviewController(ReviewRepository reviewRepo, TourRepository tourRepo, UserRepository userRepo, BookingRepository bookingRepo) {
        this.reviewRepo = reviewRepo;
        this.tourRepo = tourRepo;
        this.userRepo = userRepo;
        this.bookingRepo = bookingRepo;
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

    @GetMapping("/api/v1/listing/{id}/firstfivereviews")
    public ResponseObject getFirstFiveReviewsByListing(@PathVariable String id){
        List<Review> reviewList = reviewRepo.findFirst5ByListingIdOrderByDateCreatedDesc(id);

        return new ResponseObject(true, reviewList);
    }

    @GetMapping("/api/v1/listing/{id}/allreviews")
    public ResponseObject getReviewsByListing(@PathVariable String id){
        List<Review> reviewList = reviewRepo.findAllByListingIdOrderByDateCreatedDesc(id);

        return new ResponseObject(true, reviewList);
    }

    @GetMapping("/api/v1/user/{username}/firstfivereviews")
    public ResponseObject getFirstFiveReviewsByUsername(@PathVariable String username){
        User user = userRepo.findByUsername(username);
        if (user == null)
            return new ResponseObject(false, "Username not found");
        List<Review> reviewList = reviewRepo.findFirst5ByRevieweeIdOrderByDateCreatedDesc(user.getId());

        return new ResponseObject(true, reviewList);
    }

    // MUST BE AUTHORISED AS USER
    // *** GET ***
    // Gets all the reviews created by the logged-in user.
    // Authenticates using Bearer token in headers.
    @GetMapping("/api/v1/user/{username}/allreviews")
    public ResponseObject getReviewsByUser(@PathVariable String username){
        User user = userRepo.findByUsername(username);
        if (user == null)
            return new ResponseObject(false, "Username not found");

        List<Review> reviewList = reviewRepo.findByRevieweeIdOrderByDateCreatedDesc(user.getId());
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
        Review toAdd;
        dto.validate();
        if (dto.hasErrors()){return new ResponseObject(false, dto.getErrors());}

        String listingId = dto.getListingId();
        Optional<TourListing> listing = tourRepo.findById(dto.getListingId());
        Optional<Booking> booking = bookingRepo.findById(dto.getBookingId());
        WayfareUserDetails user = getCurrentUserDetails();


        String revieweeId;
        boolean reviewerIsWayfarer = false;

        if (listing.isEmpty())
            return new ResponseObject(false,"No such listing");

        if (booking.isEmpty())
            return new ResponseObject(false,"No booking found");

        // if you are the person that made the listing, and made the booking, and is making the review 
        // you are reviewing yourself
        if(Objects.equals(user.getId(), booking.get().getUserId()) & Objects.equals(user.getId(), listing.get().getUserId())) 
            return new ResponseObject(false, "You cannot review your own listing");

        // otherwise, if you are the person who made the booking, means that you are reviewing a Wayfarer
        if(Objects.equals(user.getId(), booking.get().getUserId()))
            revieweeId = listing.get().getUserId();

        //otherwise, if you are the person that made the listinhg, you are the Wayfarer reviewing the customer
        else if(Objects.equals(user.getId(), listing.get().getUserId())){
            revieweeId = booking.get().getUserId(); 
            reviewerIsWayfarer = true;
        }

        //then this means you are neither and you shouldn't be here;
        else
            revieweeId = null;

        if(!tourRepo.existsById(listingId))
            return new ResponseObject(false, "Listing does not exist");

        if(reviewRepo.existsByBookingIdAndUserId(dto.getBookingId(), user.getId()))
            return new ResponseObject(false, "Review already exists");

        if (revieweeId == null){
            return new ResponseObject(false, "You shouldn't be here.");
        }

        try {
            toAdd = new Mapper(tourRepo).toReview(dto, userRepo.findByUsername(user.getUsername()), revieweeId);
        }
        catch (NoSuchElementException e){
            return new ResponseObject(false,"No such listing");
        }

        if (toAdd == null)
            return new ResponseObject(false, "Server error");

        reviewRepo.save(toAdd);

        if (!reviewerIsWayfarer)
            updateListingRatings(listing.get());

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

        TourListing listing = tourRepo.findById(toDelete.getListing().getId()).orElseThrow();

        if (!Objects.equals(toDelete.getUser().getId(), user.getId()))
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

