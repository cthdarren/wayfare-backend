package com.wayfare.backend.repository;

import com.wayfare.backend.model.Shorts;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShortsRepository extends MongoRepository<Shorts, String> {
}
