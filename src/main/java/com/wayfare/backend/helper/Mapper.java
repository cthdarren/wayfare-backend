package com.wayfare.backend.helper;

import com.wayfare.backend.model.*;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.dto.UserDTO;

import java.time.Instant;

public class Mapper {
    public User toUser(UserDTO userCreationDTO)
    {
        return new User(
                userCreationDTO.getUsername(),
                userCreationDTO.getFirstName(),
                userCreationDTO.getLastName(),
                userCreationDTO.getPlainPassword(),
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
                reviewCreationDTO.getListingId()
        );
    }

    public TourListing toTourListing(TourListingDTO tourListingCreationDTO, String userId){
        return new TourListing(
                tourListingCreationDTO.getTitle(),
                tourListingCreationDTO.getDescription(),
                tourListingCreationDTO.getLocation(),
                tourListingCreationDTO.getTimeRangeList(),
                tourListingCreationDTO.getAdultPrice(),
                tourListingCreationDTO.getChildPrice(),
                tourListingCreationDTO.getMaxPax(),
                tourListingCreationDTO.getMinPax(),
                0,
                0,
                userId
        );
    }
}
