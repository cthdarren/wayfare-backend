package com.wayfare.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.wayfare.backend.model.Comment;
import com.wayfare.backend.model.CommentWithUser;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByJourneyId();
    List<Comment> findAllByJourneyIdOrderByDateCreatedDesc();
    List<Comment> findAllByJourneyIdOrderByDateCreatedAsc();
    List<Comment> findAllByUserId();

    int deleteAllByJourneyId(String journeyId);

    @Aggregation(pipeline = {
        "{ $match: { journeyId: ?0}}",
        """
  {
    $addFields: {
      objectUserId: {
        $toObjectId: "$userId",
      },
    },
  }""",
          """
  {
    $lookup: {
      from: "users",
      localField: "objectUserId",
      foreignField: "_id",
      as: "user",
    },
  },
""",
        
        """
  {
    $unwind: {
      path: "$user",
    },
  },
]      
                """
    })
    List<CommentWithUser> findAllJoinUserByJourneyId(String journeyId);
}
