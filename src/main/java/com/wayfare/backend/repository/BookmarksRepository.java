package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.Bookmark;
import com.wayfare.backend.response.BookmarkResponse;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookmarksRepository extends MongoRepository<Bookmark, String> {
    @Nullable
    @Aggregation(pipeline = {
            "{ $match : { userId: ?0 }}",
    """
    {
    $lookup: {
      from: "users",
      let: {
        searchId: {
          $toObjectId: "$listing.userId",
        },
      },
      pipeline: [
        {
          $match: {
            $expr: {
              $eq: ["$_id", "$$searchId"],
            },
          },
        },
        {
          $project: {
            username: 1,
            pictureUrl: 1,
          },
        },
      ],
      as: "user",
    },
  },
""",
            "{ $unwind : { path: $user }}"})
    List<BookmarkResponse> findAllBookmarks(String userId);
    Bookmark findByListingIdAndUserId(String listingId, String userId);
    boolean existsByListingIdAndUserId(String listingId, String userId);
}
