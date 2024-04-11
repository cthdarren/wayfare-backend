package com.wayfare.backend.helper;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.TimeZoneApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.wayfare.backend.model.*;
import com.wayfare.backend.model.dto.BookingDTO;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.model.dto.ShortsDTO;
import com.wayfare.backend.model.dto.TourListingDTO;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.model.object.PublicUserData;
import com.wayfare.backend.repository.BookingRepository;
import com.wayfare.backend.repository.ShortsRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.security.WayfareUserDetails;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.wayfare.backend.helper.helper.geoApiContext;
import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class Mapper {
    private ShortsRepository shortsRepo;
    private TourRepository tourRepo;
    private BookingRepository bookingRepo;

    public Mapper(){}
    public Mapper(TourRepository tourRepo) {
        this.tourRepo = tourRepo;
    }
    public Mapper(BookingRepository bookingRepo) {this.bookingRepo = bookingRepo;}
    public Mapper(ShortsRepository shortsRepo) {
        this.shortsRepo = shortsRepo;
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
                userCreationDTO.getUsername().toLowerCase(),
                StringUtils.capitalize(userCreationDTO.getFirstName()),
                StringUtils.capitalize(userCreationDTO.getLastName()),
                encryptedPassword,
                userCreationDTO.getEmail().toLowerCase(),
                userCreationDTO.getPhoneNumber(),
                RoleEnum.ROLE_USER,
                false,
                Instant.now(),
                Instant.now(),
                userCreationDTO.getLanguagesSpoken()
        );

    }

    public Review toReview(ReviewDTO reviewCreationDTO, User user){
        return new Review(
                reviewCreationDTO.getTitle(),
                reviewCreationDTO.getScore(),
                reviewCreationDTO.getReviewContent(),
                Instant.now(),
                Instant.now(),
                new PublicUserData(user),
                tourRepo.findById(reviewCreationDTO.getListingId()).orElseThrow(),
                reviewCreationDTO.getBookingId()
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

    public Booking toBooking(BookingDTO bookingDTO) throws IOException, InterruptedException, ApiException {
        TourListing tourListing = tourRepo.findById(bookingDTO.getListingId()).orElseThrow();
        WayfareUserDetails user = getCurrentUserDetails();
        String userId = user.getId();

        // user inputs date, stored in BookingDTO
        // mapper gets timezone from listing location
        // mapper parses new date using date input from BookingDTO and timezone data
        // new date format recorded in database is in ISO format and includes timezone offset

        return new Booking(
                tourListing,
                userId,
                bookingDTO.getBookingDuration(),
                bookingDTO.getDateBooked().toInstant().plus(bookingDTO.getBookingDuration().startTime, ChronoUnit.HOURS),
                bookingDTO.getDateBooked(),
                bookingDTO.getBookingPrice(),
                bookingDTO.getPax(),
                bookingDTO.getRemarks(),
                BookingStatusEnum.RESERVED
        );
    }

    public Shorts toShortsNoTour(ShortsDTO shortsDTO) throws IOException, InterruptedException, ApiException {
        //TourListing tourListing = tourRepo.findById(listingId).orElseThrow();
        WayfareUserDetails user = getCurrentUserDetails();
        String userId = user.getId();
        String userName = user.getUsername();
        ArrayList<String> likes = new ArrayList<>();
        return new Shorts(
                shortsDTO.getShortsUrl(),
                userName,
                userId,
                shortsDTO.getDescription(),
                shortsDTO.getDatePosted(),
                null,
                likes
        );
    }
    public Shorts toShorts(ShortsDTO shortsDTO, String listingId) throws IOException, InterruptedException, ApiException {
        TourListing tourListing = tourRepo.findById(listingId).orElseThrow();
        WayfareUserDetails user = getCurrentUserDetails();
        String userId = user.getId();
        String userName = user.getUsername();
        ArrayList<String> likes = new ArrayList<>();
        return new Shorts(
                shortsDTO.getShortsUrl(),
                userName,
                userId,
                shortsDTO.getDescription(),
                shortsDTO.getDatePosted(),
                tourListing,
                likes
        );
    }


}
