package com.wayfare.backend.helper;

import com.wayfare.backend.model.*;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.repository.TourRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;

public class Mapper {
    private TourRepository tourRepo;

    public Mapper(){}
    public Mapper(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }

    public User toUser(UserDTO userCreationDTO)
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(userCreationDTO.getPlainPassword());
        return new User(
                userCreationDTO.getPictureUrl(),
                userCreationDTO.getAboutMe(),
                new ArrayList<BadgeEnum>(),
                userCreationDTO.getUsername(),
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                encryptedPassword,
                userCreationDTO.getEmail(),
                userCreationDTO.getPhoneNumber(),
                RoleEnum.ROLE_USER,
                false,
                Instant.now(),
                Instant.now());
    }

    public Review toReview(ReviewDTO reviewCreationDTO, String userId){
        return new Review(
                reviewCreationDTO.getTitle(),
                reviewCreationDTO.getScore(),
                reviewCreationDTO.getReviewContent(),
                Instant.now(),
                Instant.now(),
                userId,
                tourRepo.findById(reviewCreationDTO.getListingId()).orElseThrow()
        );
    }

    public TourListing toTourListing(TourListingDTO tourListingCreationDTO, String userId){
        return new TourListing(
                tourListingCreationDTO.getTitle(),
                tourListingCreationDTO.getDescription(),
                tourListingCreationDTO.getThumbnailUrls(),
                tourListingCreationDTO.getCategory(),
                tourListingCreationDTO.getLocation(),
                tourListingCreationDTO.getTimeRangeList(),
                tourListingCreationDTO.getPrice(),
                tourListingCreationDTO.getMaxPax(),
                tourListingCreationDTO.getMinPax(),
                0,
                0,
                userId
        );
    }
}
