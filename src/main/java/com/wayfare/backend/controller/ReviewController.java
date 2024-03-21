package com.wayfare.backend.controller;

import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("review")
public class ReviewController {
    private final ReviewRepository reviewRepo;

    private final UserRepository userRepo;


    public ReviewController(ReviewRepository reviewRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
    }
}
