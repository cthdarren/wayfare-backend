package com.wayfare.backend.controller;

import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Bookmark;
import com.wayfare.backend.model.Review;
import com.wayfare.backend.model.TourListing;
import com.wayfare.backend.model.dto.ReviewDTO;
import com.wayfare.backend.repository.*;
import com.wayfare.backend.request.DeleteReviewRequest;
import com.wayfare.backend.request.ListingIdRequest;
import com.wayfare.backend.response.BookmarkResponse;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.security.WayfareUserDetails;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static com.wayfare.backend.helper.helper.getCurrentUserDetails;
import static java.net.http.HttpClient.newHttpClient;

@RestController
public class CurrencyController{

    private final CurrencyRepository currencyRepo;

    public CurrencyController(CurrencyRepository currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    @GetMapping("/api/v1/getrates")
    public ResponseObject getRates(){
        Object currencyData = currencyRepo.findAll().get(0).getData();
        if (currencyData == null){
            return new ResponseObject(false, "No currency data");
        }
        return new ResponseObject(true, currencyData);
    }
}

