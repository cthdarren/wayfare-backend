package com.wayfare.backend.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.VerifyURL;
import com.wayfare.backend.repository.VerifyURLRepository;
import com.wayfare.backend.request.PasswordForWayfarerSignUpRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.exception.FormatException;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.LoginRequest;
import com.wayfare.backend.security.WayfareUserDetailService;
import com.wayfare.backend.security.WayfareUserDetails;
import com.wayfare.backend.security.jwt.JwtService;
import static com.wayfare.backend.helper.helper.getCurrentUserDetails;
import static com.wayfare.backend.model.RoleEnum.ROLE_WAYFARER;

import java.net.http.HttpClient;
import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Value("${MAIL_DOMAIN_NAME}")
    private String MAIL_DOMAIN_NAME;

    @Value("${MAIL_API_KEY}")
    private String MAIL_API_KEY;

    private final WayfareUserDetailService wayfareUserDetailsService;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    private final UserRepository userRepo;
    private final VerifyURLRepository verifyRepo;

    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, WayfareUserDetailService wayfareUserDetailsService, UserRepository userRepo, VerifyURLRepository verifyRepo, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.wayfareUserDetailsService = wayfareUserDetailsService;
        this.userRepo = userRepo;
        this.verifyRepo = verifyRepo;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/api/v1/auth/register", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseObject wayfareRegister(@RequestBody UserDTO dto){
        try{
            dto.validate();
            if (userRepo.existsByUsername(dto.getUsername()))
                dto.addErrors("Username already exists");
            if (userRepo.existsByEmail(dto.getEmail()))
                dto.addErrors("Email already exists");
            if (dto.hasErrors())
                throw new FormatException(dto.getErrors());

            User newUser = new Mapper().toUser(dto);
            userRepo.save(newUser);

            String token = jwtService.generateToken(newUser);

            return new ResponseObject(true, token);//inserted.getUsername());
        }
        catch (FormatException e){
            return new ResponseObject(false, e.getErrors());
        }
        catch(DuplicateKeyException e){
            return new ResponseObject(false, "Username already exists");
        }
        catch (Exception e){
            return new ResponseObject(false, "Internal Server Error");
        }

    }
    @PostMapping("/api/v1/auth/login")
    public ResponseObject wayfareLogin(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        }
        catch (BadCredentialsException e){
            return new ResponseObject(false, "Invalid credentials");
        }

        User user = userRepo.findByUsername(loginRequest.username());
        String token = jwtService.generateToken(user);

        return new ResponseObject(true, token);
    }

    @PostMapping("/wayfarersignup")
    public ResponseObject wayfarerSignUp(@RequestBody PasswordForWayfarerSignUpRequest request) {

        WayfareUserDetails currUser = getCurrentUserDetails();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(currUser.getUsername(), request.password())
            );
        } catch (BadCredentialsException e) {
            return new ResponseObject(false, "Invalid credentials");
        }

        User user = userRepo.findByUsername(currUser.getUsername());
        if (user != null) {
            user.setRole(ROLE_WAYFARER);
            userRepo.save(user);

            return new ResponseObject(true, "You are now a wayfarer!");
        }
        return new ResponseObject(false, "Your session has expired, please log in again.");

    }

    // TODO email verification using one time link
    @GetMapping("/api/v1/verify/{oneTimeURL}")
    public ResponseObject verifyUser(@PathVariable String oneTimeURL){
        VerifyURL checkURL = verifyRepo.findByUrl(oneTimeURL);
        if (checkURL != null){
            User toVerify = userRepo.findByUsername(checkURL.getUsername());
            toVerify.setIsVerified(true);
            toVerify.setDateModified(Instant.now());
            userRepo.save(toVerify);
            verifyRepo.delete(checkURL);

            return new ResponseObject(toVerify.getIsVerified(), toVerify.getUsername());
        }
        return new ResponseObject(false, "Verify link expired");
    }

    @GetMapping("/generateVerifyLink")
    public ResponseObject generateVerifyLink(){
        WayfareUserDetails currUser = getCurrentUserDetails();

        if (currUser.getIsVerified()){
            return new ResponseObject(false, "User is already verified");
        }
        String userEmail = currUser.getEmail();

        HttpClient httpClient = HttpClient.newHttpClient();
        String postURL = "https://api.mailgun.net/v3/" + MAIL_DOMAIN_NAME + "/messages.mime";

        String randomGUID = UUID.randomUUID().toString();

        try {
            // Had to use unirest here because the normal HttpClient library has problems with multipart forms
            HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + MAIL_DOMAIN_NAME + "/messages")
                    .basicAuth("api", MAIL_API_KEY)
                    .queryString("from", "noreply@" + MAIL_DOMAIN_NAME)
                    .queryString("to", userEmail)
                    .queryString("subject", "WayFare Email Verification")
                    // .queryString("text", "verify your email by clicking this link below!\n\nhttp://localhost:8080/api/auth/verify/" + randomGUID)
                    .queryString("text", "verify your email by clicking this link below!\n\nhttp://143.198.223.202/api/auth/verify/" + randomGUID)
                    .asJson();

            if (response.getStatus() == 200) {
                VerifyURL newURL = new VerifyURL(randomGUID, currUser.getUsername());
                verifyRepo.save(newURL);
                return new ResponseObject(true, userEmail);
            }

            return new ResponseObject(false, "Sending email failed");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseObject(false, "Server error");
        }

    }




//    @PostMapping("/jwtLogin")
//    public ResponseObject jwlogin(@RequestBody LoginRequest request){
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.username(), request.password())
//        );
//
//        User user = userRepo.findByUsername(request.username());
//        String token = jwtService.generateToken(user);
//
//        return new ResponseObject(true, token);
//    }
//    @PostMapping("/jwtRegister")
//
//    public ResponseObject reg(@RequestBody RegisterRequest request){
//        User user = new User(request.username(), request.password(), request.email(), request.phoneNumber(), RoleEnum.ROLE_USER);
//
//        user = userRepo.save(user);
//
//        String token = jwtService.generateToken(user);
//
//        return new ResponseObject(true, token);
//    }
}