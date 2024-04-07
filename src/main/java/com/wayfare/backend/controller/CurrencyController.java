package com.wayfare.backend.controller;

import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Bookmark;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.repository.BookmarksRepository;
import com.wayfare.backend.repository.ReviewRepository;
import com.wayfare.backend.repository.TourRepository;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.DeleteReviewRequest;
import com.wayfare.backend.request.ListingIdRequest;
import com.wayfare.backend.response.BookmarkResponse;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;

@RestController
public class CurrencyController{
    @GetMapping("/getRates")
    public ResponseObject getRates(){
        return new ResponseObject(false, null);    
    }
}

