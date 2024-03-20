package com.wayfare.backend.repository;

import com.wayfare.backend.model.VerifyURL;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifyURLRepository extends MongoRepository<VerifyURL, String> {
    VerifyURL findByUrl(String url);
}
