package com.wayfare.backend.controller;


import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.request.EditAccountDetailsRequest;
import com.wayfare.backend.request.EditProfileRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;

import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class UserController {

    private final UserRepository userRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
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

    @PostMapping("/profile/edit")
    public ResponseObject editProfile(@RequestBody EditProfileRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toEdit = userRepo.findByUsername(user.getUsername());

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

        toEdit.setFirstName(request.firstName());
        toEdit.setLastName(request.lastName());
        toEdit.setPhoneNumber(request.phoneNumber());
        toEdit.setUsername(request.username());
        toEdit.setDateModified(Instant.now());

        toEdit.validate();

        if (toEdit.hasErrors())
            return new ResponseObject(false, toEdit.getErrors());

        userRepo.save(toEdit);
        return new ResponseObject(true, "Account details have been upated");
    }

    @PostMapping("/account/delete")
    public ResponseObject deleteAccount(@RequestBody EditAccountDetailsRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        User toDelete = userRepo.findByUsername(user.getUsername());

        userRepo.delete(toDelete);
        return new ResponseObject(true, "Account deleted");
    }

    @GetMapping("/profile/{username}")
    public ResponseObject getUserProfile(@PathVariable String username){
        userRepo.findByUsername(username);
        return new ResponseObject(true, "");
    }

}
