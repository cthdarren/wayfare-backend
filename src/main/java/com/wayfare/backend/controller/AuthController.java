package com.wayfare.backend.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.Otp;
import com.wayfare.backend.repository.OtpRepository;
import com.wayfare.backend.request.OtpRequest;
import com.wayfare.backend.request.PasswordRequest;
import com.wayfare.backend.response.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.dto.UserDTO;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.LoginRequest;
import com.wayfare.backend.request.UserEmailRequest;
import com.wayfare.backend.security.WayfareUserDetailService;
import com.wayfare.backend.security.WayfareUserDetails;
import com.wayfare.backend.security.jwt.JwtService;
import static com.wayfare.backend.helper.helper.getCurrentUserDetails;
import static com.wayfare.backend.model.RoleEnum.ROLE_WAYFARER;

import java.net.http.HttpClient;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.wayfare.backend.validator.EmailValidator;
import com.wayfare.backend.validator.UsernameValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Value("${MAIL_DOMAIN_NAME}")
    private String MAIL_DOMAIN_NAME;

    @Value("${MAIL_API_KEY}")
    private String MAIL_API_KEY;

    @Value("${SERVER_URL}")
    private String SERVER_URL;

    private final WayfareUserDetailService wayfareUserDetailsService;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    private final UserRepository userRepo;
    private final OtpRepository otpRepo;

    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, WayfareUserDetailService wayfareUserDetailsService, UserRepository userRepo, OtpRepository otpRepo, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.wayfareUserDetailsService = wayfareUserDetailsService;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.jwtService = jwtService;
    }
    @PostMapping("/api/v1/auth/checknewuseremail")
    public ResponseObject checkUserAndEmailExists(@RequestBody UserEmailRequest request){
        boolean usernameExists = userRepo.existsByUsername(request.username().toLowerCase());
        boolean emailExists = userRepo.existsByEmail(request.email().toLowerCase());

        ArrayList<String> errorList = new ArrayList<>(); 
        if (request.username() == ""){
            errorList.add("Username cannot be blank");
        }
        else if (usernameExists){
            errorList.add("Username exists");
        }
        else{
            errorList.add(new UsernameValidator(request.username().toLowerCase()).validateRegex());
        }
        if (request.email() == ""){
            errorList.add("Email cannot be blank");
        }
        else if (emailExists){
            errorList.add("Email exists");
        }
        else{
            errorList.add(new EmailValidator(request.email().toLowerCase()).validateRegex());
        }

        if (errorList.get(0) == null & errorList.get(1) == null){
            return new ResponseObject(true, "username and email does not exist");
        }
        else{
            return new ResponseObject(false, errorList);
        }
    }


    @PostMapping(value = "/api/v1/auth/register", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseObject wayfareRegister(@RequestBody UserDTO dto){
            dto.validate();
            if (userRepo.existsByUsername(dto.getUsername()))
                dto.addErrors("Username already exists");
            if (userRepo.existsByEmail(dto.getEmail()))
                dto.addErrors("Email already exists");

            if (dto.hasErrors()) {
                return new ResponseObject(false, dto.getErrors());
            }

        try{
            User newUser = new Mapper().toUser(dto);
            userRepo.save(newUser);

            String token = jwtService.generateToken(newUser);

            return new ResponseObject(true, token);//inserted.getUsername());
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

    /// MUST BE AUTHORISED AS USER
    // *** POST ***
    // Used for users to sign up as a Wayfarer. Should only be able to be done when user
    // is logged in to the app. Takes in Bearer token through headers, and requests the user
    // for his password one more time for verification.

    // Accepts application/json
    // {"password": USERPASSWORD}
    @PostMapping("/wayfarersignup")
    public ResponseObject wayfarerSignUp(@RequestBody PasswordRequest request) {

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

//    //TODO email verification using one time link
//    @GetMapping("/api/v1/verify/{oneTimeURL}")
//    public ResponseObject verifyUser(@PathVariable String oneTimeURL){
//        Otp checkURL = otpRepo.findByUrl(oneTimeURL);
//        if (checkURL != null){
//            User toVerify = userRepo.findByUsername(checkURL.getUsername());
//            toVerify.setIsVerified(true);
//            toVerify.setDateModified(Instant.now());
//            userRepo.save(toVerify);
//            otpRepo.delete(checkURL);
//
//            return new ResponseObject(toVerify.getIsVerified(), toVerify.getUsername());
//        }
//        return new ResponseObject(false, "Verify link expired");
//    }

    @PostMapping("/verifyotp")
    public ResponseObject verifyOtp(@RequestBody OtpRequest request){
        WayfareUserDetails user = getCurrentUserDetails();
        List<Otp> otpList = otpRepo.findAllByUserIdAndCreationTimeGreaterThan(user.getId(), LocalDateTime.now().minusMinutes(10));
        for (Otp otp : otpList){
            if (Objects.equals(otp.getOtpString(), request.otp()) & Objects.equals(otp.getUserId(), user.getId())) {
                User toVerify = userRepo.findById(user.getId()).orElseThrow();
                toVerify.setIsVerified(true);
                userRepo.save(toVerify);

                otpRepo.deleteByUserId(user.getId());

                return new ResponseObject(true, "You have been verified");
            }
        }
        return new ResponseObject(false, "Otp not found or expired");
    }

        /// MUST BE AUTHORISED AS USER
    // *** GET ***
    // Generates verify link for the specified user. Needs the user to be logged in and pass
    // the users Bearer token through the headers. On success, returns the user email to be
    // displayed in the front end ( can use a message similar to please check your email at {email} )
    @GetMapping("/generateotp")
    public ResponseObject generateOtp(){
        WayfareUserDetails currUser = getCurrentUserDetails();

        if (currUser.getIsVerified()){
            return new ResponseObject(false, "User is already verified");
        }
        String userEmail = currUser.getEmail();

        HttpClient httpClient = HttpClient.newHttpClient();
        String postURL = "https://api.mailgun.net/v3/" + MAIL_DOMAIN_NAME + "/messages.mime";

        int otp = new Random().nextInt(999999);
        String otpString = String.format("%06d", otp);

        try {
            // Had to use unirest here because the normal HttpClient library has problems with multipart forms
            HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + MAIL_DOMAIN_NAME + "/messages")
                    .basicAuth("api", MAIL_API_KEY)
                    .queryString("from", "noreply@" + MAIL_DOMAIN_NAME)
                    .queryString("to", userEmail)
                    .queryString("subject", "WayFare Email Verification")
                    // .queryString("text", "verify your email by clicking this link below!\n\nhttp://localhost:8080/api/auth/verify/" + randomGUID)
//                    .queryString("text", "Verify your email by clicking this link below!\n\n" + SERVER_URL + "/api/auth/verify/" + randomGUID)
                    .queryString("text", "Your otp is " + otpString)
                    .asJson();

            if (response.getStatus() == 200) {
                Otp newOtp = new Otp(otpString, currUser.getId());
                otpRepo.save(newOtp);
                return new ResponseObject(true, userEmail);
            }

            return new ResponseObject(false, "Sending email failed");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseObject(false, "Server error");
        }

    }
}