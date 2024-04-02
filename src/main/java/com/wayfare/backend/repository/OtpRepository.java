package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface OtpRepository extends MongoRepository<Otp, String> {
    @Nullable
    List<Otp> findAllByUserIdAndCreationTimeGreaterThan(String userId, LocalDateTime time);
    int deleteByUserId(String userId);
}
