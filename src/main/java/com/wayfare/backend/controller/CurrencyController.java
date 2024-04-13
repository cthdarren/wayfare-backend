package com.wayfare.backend.controller;

import com.wayfare.backend.helper.helper;
import com.wayfare.backend.model.Currency;
import com.wayfare.backend.repository.*;
import com.wayfare.backend.response.ResponseObject;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RestController
public class CurrencyController{

    private final CurrencyRepository currencyRepo;

    public CurrencyController(CurrencyRepository currencyRepo) {
        this.currencyRepo = currencyRepo;
    }

    @GetMapping("/api/v1/getrates")
    public ResponseObject getRates(){
        try {
            Currency currencyData = currencyRepo.findAll().get(0);
            if (ChronoUnit.DAYS.between(Instant.now(), currencyData.getLastUpdate()) != 0 )
                new helper(currencyRepo).getExchangeRates();

            return new ResponseObject(true, currencyData.getData());
        }
        catch (IndexOutOfBoundsException e) {
            return new ResponseObject(false, "No currency data");
        }
    }
}

