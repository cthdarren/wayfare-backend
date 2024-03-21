package com.wayfare.backend.controller;


import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public UserController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/{username}")
    public ResponseObject getUser(@PathVariable("username") String username){
        ResponseObject result;
        User test = userRepo.findByUsername(username);
        if (test != null)
            result = new ResponseObject(true, test);
        else
            result = new ResponseObject(false, "User not found");

        return result;

    }

    @GetMapping("/home")
    public ResponseObject home(){
        return new ResponseObject(true, "you are a user!");
    }

    @GetMapping("/wayfarer")
    public ResponseObject wayfarer(){
        return new ResponseObject(true, "you are a wayfarer!");
    }

}
