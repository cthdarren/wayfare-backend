package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.VerifyURL;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerifyURLRepository extends MongoRepository<VerifyURL, String> {
    @Nullable
    VerifyURL findByUrl(String url);
}
