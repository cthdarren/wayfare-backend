package com.wayfare.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class LoginController {
    @GetMapping("/{id}")
    public ResponseObject getUser(@PathVariable("id") String id){
        System.out.println("test'");
        return new ResponseObject(true, "ol");

    }
    @PostMapping(value = "/login", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseObject createUser(@RequestBody User user){
        System.out.println("iswear");
        return new ResponseObject(true, "fax");

    }


}
