package com.wayfare.backend.controller;

import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.exception.FormatException;
import com.wayfare.backend.model.RoleEnum;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.UserCreationDTO;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.request.LoginRequest;
import com.wayfare.backend.request.RegisterRequest;
import com.wayfare.backend.security.WayfareUserDetailService;
import com.wayfare.backend.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import static com.wayfare.backend.model.RoleEnum.ROLE_USER;
import static com.wayfare.backend.model.RoleEnum.ROLE_WAYFARER;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;


    private final WayfareUserDetailService wayfareUserDetailsService;
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    private final UserRepository userRepo;

    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, WayfareUserDetailService wayfareUserDetailsService, UserRepository userRepo, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.wayfareUserDetailsService = wayfareUserDetailsService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/register", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseObject wayfareRegister(@RequestBody RegisterRequest registerRequest){
        try{
            UserCreationDTO toInsert = new UserCreationDTO(registerRequest.username(),  registerRequest.password(), registerRequest.verifyPassword(), registerRequest.email(), registerRequest.phoneNumber(), ROLE_USER);
            User newUser = wayfareUserDetailsService.registerUser(toInsert);
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
    @PostMapping("/login")
    public ResponseObject wayfareLogin(@RequestBody LoginRequest loginRequest) {
//        Authentication authenticationRequest =
//                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
//        Authentication authenticationResponse =
//                this.authenticationManager.authenticate(authenticationRequest);
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
        // ...
//        return new ResponseObject(true, authenticationResponse.getName());
    }

    @PostMapping("/wayfarersignup")
    public ResponseObject wayfarerSignUp(@RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            return new ResponseObject(false, "Invalid credentials");
        }

        User user = userRepo.findByUsername(loginRequest.username());
        user.setRole(ROLE_WAYFARER);
        userRepo.save(user);

        return new ResponseObject(true, "you are now a wayfarer!");
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