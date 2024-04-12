package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.Role;
import com.wayfare.backend.model.RoleEnum;
import com.wayfare.backend.model.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    // all the reviews that someone has created
    List<Review> findAllByUserId(String userId);

    //all the reviews that has been made by other people for this person
    List<Review> findAllByRevieweeId(String revieweeId);

    List<Review> findAllByListingId(String listingId);
    List<Review> findAllByListingIdOrderByDateCreatedDesc(String listingId);
    List<Review> findFirst5ByListingIdOrderByDateCreatedDesc(String listingId);
    List<Review> findFirst5ByListingUserIdOrderByDateCreatedDesc(String UserId);

    List<Review> findFirst5ByRevieweeIdOrderByDateCreatedDesc(String UserId);
    List<Review> findByRevieweeIdOrderByDateCreatedDesc(String UserId);

    @Aggregation(pipeline = {
            "{ $match : { \"listing.userId\": ?0, revieweeId: ?0} }",
            "{ $group : { _id : null, total: { $count: {} } } }",
    })
    Integer findNumberOfReviewsByCustomers(String userId);

    @Aggregation(pipeline = {
            "{ $match : { \"revieweeId\": ?0} }",
            "{ $group : { _id : null, total: { $count: {} } } }",
    })
    Integer findNumberOfReviewsByOthers(String revieweeId);
    
    @Nullable
    Review findByUserIdAndListingId(String userId, String listingId);
    boolean existsByUserIdAndListingId(String userId, String listingId);
    int deleteAllByListingId(String listingId);
    boolean existsByBookingIdAndUserId(String bookingId, String userId);
}
