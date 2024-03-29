package com.wayfare.backend.helper;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.wayfare.backend.model.*;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.model.object.PublicUserData;
import com.wayfare.backend.repository.TourRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.wayfare.backend.helper.helper.geoApiContext;


@RestController
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
        String picUrl = userCreationDTO.getPictureUrl();
        if (userCreationDTO.getPictureUrl() == null)
            picUrl = "";
        return new User(
                picUrl,
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

    public Review toReview(ReviewDTO reviewCreationDTO, User user){
        return new Review(
                reviewCreationDTO.getTitle(),
                reviewCreationDTO.getScore(),
                reviewCreationDTO.getReviewContent(),
                Instant.now(),
                Instant.now(),
                new PublicUserData(user),
                tourRepo.findById(reviewCreationDTO.getListingId()).orElseThrow()
        );
    }

    public TourListing toTourListing(TourListingDTO tourListingCreationDTO, String userId) throws IOException, InterruptedException, ApiException {
        Double lat = tourListingCreationDTO.getLocation().getY();
        Double lng = tourListingCreationDTO.getLocation().getX();

        GeocodingResult[] result =  GeocodingApi.reverseGeocode(
                geoApiContext,
                new LatLng(lat,lng))
                .resultType( AddressType.ADMINISTRATIVE_AREA_LEVEL_1, AddressType.COUNTRY)
                .await();

        return new TourListing(
                tourListingCreationDTO.getTitle(),
                tourListingCreationDTO.getDescription(),
                tourListingCreationDTO.getThumbnailUrls(),
                tourListingCreationDTO.getCategory(),
                tourListingCreationDTO.getLocation(),
                result[0].formattedAddress,
                tourListingCreationDTO.getTimeRangeList(),
                tourListingCreationDTO.getPrice(),
                tourListingCreationDTO.getMaxPax(),
                tourListingCreationDTO.getMinPax(),
                0,
                0,
                userId
        );
    }

    public Booking toBooking(BookingDTO bookingDTO, String userId) throws IOException, InterruptedException, ApiException {
        List<TourListing> tourListingId = tourRepo.findAllByUserId(userId);
        return new Booking(
                tourListingId.toString(),
                userId,
                bookingDTO.getBookingDuration(),
                bookingDTO.getDateBooked(),
                bookingDTO.getBookingPrice(),
                bookingDTO.getPax(),
                bookingDTO.getRemarks(),
                bookingDTO.getStatus()
        );
    }


}
