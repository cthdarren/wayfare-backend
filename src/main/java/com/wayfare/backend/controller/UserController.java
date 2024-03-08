package com.wayfare.backend.controller;


import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    @GetMapping("/user")
    public List<User> user() {
        System.out.println("ACCESSED API");
        List<User> test = userRepo.findByFirstName("test");
        User first = test.get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try{
            String json = mapper.writeValueAsString(first);
            System.out.println(json);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return test;

    }
    @PostMapping(value = "/user/create", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> createUser(@RequestBody User user) throws Exception {
        System.out.println("Test");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        System.out.println(json);
        try{
            String success = "true";
            userRepo.save(new User("test", "lmoa", "test", "last", "1928312", "spingpoing"));
            return ResponseEntity.ok(success);
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok("failure");
        }
    }
}
