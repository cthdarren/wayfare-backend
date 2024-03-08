package com.wayfare.backend.controller;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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
    public List<User> user(@RequestParam(value = "firstName") String name) {
        System.out.println("ACCESSED API WITH" + name);
        System.out.println(userRepo.findAll());
        return userRepo.findByFirstName(name);
    }
    @PostMapping(value = "/user/create", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> createUser(@RequestBody User user) throws Exception {
        System.out.println(user.getFirstName());
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
