package com.wayfare.backend.controller;


import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;

import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepo;

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @GetMapping("/user/{id}")
    public ResponseObject getUser(@PathVariable("id") String id){
        ResponseObject result;
        // Optional classes https://www.baeldung.com/java-optional
        Optional<User> test = userRepo.findById(id);
        if (test.isPresent())
            result = new ResponseObject(true, test.get());
        else
            result = new ResponseObject(false, null);
        
        //purely for debugging purposes and seeing results in console
        try{
            String json = mapper.writeValueAsString(result);
            System.out.println(json);
        }
        catch (JsonProcessingException e){ 
            e.printStackTrace();
        }

        return result;

    }
    @PostMapping(value = "/user/create", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> createUser(@RequestBody User user) throws Exception {
        System.out.println("Test");
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
