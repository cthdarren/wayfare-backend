package com.wayfare.backend.controller;


import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.request.EditAccountDetailsRequest;
import com.wayfare.backend.request.EditProfileRequest;
import com.wayfare.backend.request.PasswordRequest;
import com.wayfare.backend.response.AccountSettingsResponse;
import com.wayfare.backend.response.ProfileResponse;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;

import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final ReviewRepository reviewRepo;
    private final TourRepository tourRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public UserController(AuthenticationManager authenticationManager, UserRepository userRepo, ReviewRepository reviewRepo, TourRepository tourRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.reviewRepo = reviewRepo;
        this.tourRepo = tourRepo;
    }

    @GetMapping("/api/v1/user/{username}")
    public ResponseObject getUser(@PathVariable("username") String username){
        ResponseObject result;
        User test = userRepo.findByUsername(username);

        if (test != null)
            result = new ResponseObject(true, test);
        else
            result = new ResponseObject(false, "User not found");

        return result;
    }

    @GetMapping("/account")
    public ResponseObject getAccount(){
        WayfareUserDetails user = getCurrentUserDetails();
        User currUser = userRepo.findByUsername(user.getUsername());

        if (currUser == null)
            return new ResponseObject(false, "No such user with username " + user.getUsername());

        AccountSettingsResponse response = new AccountSettingsResponse(
                currUser.getUsername(),
                currUser.getFirstName(),
                currUser.getLastName(),
                currUser.getEmail(),
                currUser.getPhoneNumber(),
                currUser.getIsVerified(),
                currUser.getDateCreated(),
                currUser.getPictureUrl(),
                currUser.getAboutMe(),
                currUser.getBadges(),
                currUser.getRole()
        );
        return new ResponseObject(true, response);

    }

    @PostMapping("/profile/edit")
    public ResponseObject editProfile(@RequestBody EditProfileRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toEdit = userRepo.findByUsername(user.getUsername());
        if (toEdit == null)
            return new ResponseObject(false, "Username not found");

        toEdit.setPictureUrl(request.pictureUrl());
        toEdit.setAboutMe(request.aboutMe());
        toEdit.setDateModified(Instant.now());

        userRepo.save(toEdit);
        return new ResponseObject(true, "Profile has been edited");
    }

    @PostMapping("/account/edit")
    public ResponseObject editAccountInfo(@RequestBody EditAccountDetailsRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toEdit = userRepo.findByUsername(user.getUsername());

        request.validate();

        if(userRepo.existsByUsername(request.username))
            request.addErrors("Username already exists");

        if (request.hasErrors())
            return new ResponseObject(false, request.getErrors());

        toEdit.setFirstName(request.firstName);
        toEdit.setLastName(request.lastName);
        toEdit.setPhoneNumber(request.phoneNumber);
        toEdit.setUsername(request.username);
        toEdit.setDateModified(Instant.now());

        userRepo.save(toEdit);
        return new ResponseObject(true, "Account details have been upated");
    }

    @PostMapping("/account/delete")
    public ResponseObject deleteAccount(@RequestBody PasswordRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toDelete = userRepo.findByUsername(user.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
            );
        } catch (BadCredentialsException e) {
            return new ResponseObject(false, "Invalid credentials");
        }
        if (toDelete == null)
            return new ResponseObject(false, "Username not found");

        userRepo.delete(toDelete);
        return new ResponseObject(true, "Account deleted");
    }

    //TODO GET AVERAGE REVIEWS FROM ALL AVERAGE REVIEW LISTINGS
    @GetMapping("/api/v1/profile/{username}")
    public ResponseObject getUserProfile(@PathVariable String username){
        User toView = userRepo.findByUsername(username);
        if (toView == null){
            return new ResponseObject(false, "Username does not exist");
        }
        List<Review> userReviews = reviewRepo.findFirst5ByListingUserIdOrderByDateCreatedDesc(toView.getId());
        List<TourListing> userTours = tourRepo.findAllByUserId(toView.getId());
        Double avgScore = tourRepo.avgScoreByUserId(toView.getId());
        Integer reviewCount = reviewRepo.findNumberOfReviewsByCustomers(toView.getId());

        if (avgScore == null)
            avgScore = 0.0;

        if (reviewCount == null)
            reviewCount = 0;

        ProfileResponse response = new ProfileResponse(
                username,
                toView.getFirstName(),
                toView.getLastName(),
                toView.getAboutMe(),
                toView.getPictureUrl(),
                toView.getBadges(),
                avgScore,
                reviewCount,
                toView.getRole(),
                userReviews,
                userTours,
                toView.getDateCreated()
        );

        return new ResponseObject(true, response);
    }

}
