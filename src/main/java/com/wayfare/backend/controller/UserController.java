package com.wayfare.backend.controller;


import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepository userRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @GetMapping("/{id}")
    public ResponseObject getUser(@PathVariable("id") String id){
        ResponseObject result;
        // Optional classes https://www.baeldung.com/java-optional
        Optional<User> test = userRepo.findById(id);
        if (test.isPresent())
            result = new ResponseObject(true, test.get());
        else
            result = new ResponseObject(false, "User not found");
        
//        //purely for debugging purposes and seeing results in console
//        try{
//            String json = mapper.writeValueAsString(result);
//            System.out.println(json);
//        }
//        catch (JsonProcessingException e){
//            e.printStackTrace();
//        }

        return result;

    }



}
