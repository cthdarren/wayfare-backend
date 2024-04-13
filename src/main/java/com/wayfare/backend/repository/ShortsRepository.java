package com.wayfare.backend.repository;

import com.wayfare.backend.model.Shorts;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortsRepository extends MongoRepository<Shorts, String> {
    // @Aggregation()
    // List<Shorts> findAllJoinComments();
    List<Shorts> findAllByUserId(String userId);
}
