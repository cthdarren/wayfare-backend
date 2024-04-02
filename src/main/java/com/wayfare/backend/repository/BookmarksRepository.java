package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.Bookmark;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookmarksRepository extends MongoRepository<Bookmark, String> {
    @Nullable
    List<Bookmark> findAllByUserId(String userId);
    Bookmark findByListingIdAndUserId(String listingId, String userId);
    boolean existsByListingIdAndUserId(String listingId, String userId);
}
