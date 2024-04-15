package com.wayfare.backend.controller;


import static com.wayfare.backend.helper.helper.getCurrentUserDetails;
import static com.wayfare.backend.model.RoleEnum.ROLE_WAYFARER;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.Shorts;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.ShortsRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.EditAccountDetailsRequest;
import com.wayfare.backend.request.EditProfileRequest;
import com.wayfare.backend.request.PasswordRequest;
import com.wayfare.backend.response.AccountSettingsResponse;
import com.wayfare.backend.response.ProfileResponse;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;

@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final ReviewRepository reviewRepo;
    private final TourRepository tourRepo;
    private final ShortsRepository shortsRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public UserController(AuthenticationManager authenticationManager, UserRepository userRepo, ReviewRepository reviewRepo, TourRepository tourRepo, ShortsRepository shortsRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.reviewRepo = reviewRepo;
        this.tourRepo = tourRepo;
        this.shortsRepo = shortsRepo;
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
                currUser.getId(),
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
                currUser.getRole(),
                currUser.getLanguagesSpoken()
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
        toEdit.setLanguagesSpoken(request.languagesSpoken());
        toEdit.setDateModified(Instant.now());

        userRepo.save(toEdit);
        return new ResponseObject(true, "Profile has been edited");
    }

    @PostMapping("/account/edit")
    public ResponseObject editAccountInfo(@RequestBody EditAccountDetailsRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toEdit = userRepo.findByUsername(user.getUsername());

        request.validate();

        if (!Objects.equals(user.getEmail(), toEdit.getEmail())) {
            if (userRepo.existsByEmail(request.email))
                request.addErrors("Email already exists");
        }

        if (request.hasErrors())
            return new ResponseObject(false, request.getErrors());

        toEdit.setFirstName(request.firstName);
        toEdit.setLastName(request.lastName);
        toEdit.setEmail(request.email);
        toEdit.setPhoneNumber(request.phoneNumber);
        toEdit.setDateModified(Instant.now());

        userRepo.save(toEdit);
        return new ResponseObject(true, "Account details have been updated");
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
        List<Review> userReviews = reviewRepo.findByRevieweeIdOrderByDateCreatedDesc(toView.getId());
        List<TourListing> userTours = tourRepo.findAllByUserId(toView.getId());

        Double avgScore;
        Integer reviewCount; 
        if (toView.getRole() == ROLE_WAYFARER){
            reviewCount = reviewRepo.findNumberOfReviewsByCustomers(toView.getId());
            avgScore = tourRepo.avgScoreByUserId(toView.getId());
        }
        else{
            reviewCount = reviewRepo.findNumberOfReviewsByOthers(toView.getId());
            avgScore = null;
        }


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
                toView.getLanguagesSpoken(),
                toView.getBadges(),
                avgScore,
                reviewCount,
                toView.getRole(),
                userReviews,
                userTours,
                toView.getDateCreated(),
                toView.getIsVerified()
        );

        return new ResponseObject(true, response);
    }

    //Profiel by userId
    @GetMapping("/api/v1/profileid/{userId}")
    public ResponseObject getUserProfileById(@PathVariable String userId){
        Optional<User> getUser = userRepo.findById(userId);
        if (getUser.isEmpty()){
            return new ResponseObject(false, "UserId does not exist");
        }
        User toView =getUser.get();
        List<Review> userReviews = reviewRepo.findByRevieweeIdOrderByDateCreatedDesc(toView.getId());
        List<TourListing> userTours = tourRepo.findAllByUserId(toView.getId());

        Double avgScore;
        Integer reviewCount; 
        if (toView.getRole() == ROLE_WAYFARER){
            reviewCount = reviewRepo.findNumberOfReviewsByCustomers(toView.getId());
            avgScore = tourRepo.avgScoreByUserId(toView.getId());
        }
        else{
            reviewCount = reviewRepo.findNumberOfReviewsByOthers(toView.getId());
            avgScore = null;
        }

        if (avgScore == null)
            avgScore = 0.0;

        if (reviewCount == null)
            reviewCount = 0;

        ProfileResponse response = new ProfileResponse(
                toView.getUsername(),
                toView.getFirstName(),
                toView.getLastName(),
                toView.getAboutMe(),
                toView.getPictureUrl(),
                toView.getLanguagesSpoken(),
                toView.getBadges(),
                avgScore,
                reviewCount,
                toView.getRole(),
                userReviews,
                userTours,
                toView.getDateCreated(),
                toView.getIsVerified()
        );

        return new ResponseObject(true, response);
    }

    @GetMapping("/api/v1/profileshorts/{username}")
    public ResponseObject getAllShortsByUserId(@PathVariable String username) {
        User user = userRepo.findByUsername(username);
        if (user == null){
            return new ResponseObject(false, "Username does not exist");
        }
        List<Shorts> allShorts = shortsRepo.findAllByUserIdOrderByDatePostedDesc(user.getId());
        return new ResponseObject(true, allShorts);
    }
}
