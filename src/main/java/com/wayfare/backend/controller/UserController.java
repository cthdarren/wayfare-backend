package com.wayfare.backend.controller;


import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/allusers")
    public List<User> allUsers(String name) {
        return userRepo.findAll();
   }
}
