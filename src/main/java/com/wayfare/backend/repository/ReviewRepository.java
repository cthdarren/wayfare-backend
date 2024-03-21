package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.Role;
import com.wayfare.backend.model.RoleEnum;
import com.wayfare.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByUserId(String userId);
    List<Review> findAllByListingId(String listingId);
    List<Review> findFirst5ByListingIdOrderByDateCreatedAsc(String listingId);

    @Nullable
    Review findByUserIdAndListingId(String userId, String listingId);
    boolean existsByUserIdAndListingId(String userId, String listingId);
}
