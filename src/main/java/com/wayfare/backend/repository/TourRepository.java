package com.wayfare.backend.repository;

import com.mongodb.lang.Nullable;
import com.wayfare.backend.model.TourListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.time.Instant;

public interface TourRepository extends MongoRepository<TourListing, String> {
    List<TourListing> findAllByUserId(String userId);
    List<TourListing> findByTourStartDateTime(Instant startDate);
    List<TourListing> findByTourEndDateTime(Instant endDate);
//    List<TourListing> findByDateRange(Instant startDate, Instant endDate);
    List<TourListing> findByTourName(String tourName);
    List<TourListing> findAllByCountry(String countryName);
    boolean existsByIdAndUserId(String tourId, String userId);
}
