package com.wayfare.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.UserCreationDTO;
import com.wayfare.backend.repository.UserRepository;
import com.wayfare.backend.security.WayfareUserDetailService;
import com.wayfare.backend.security.WayfareUserDetails;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

import static com.wayfare.backend.model.RoleEnum.ROLE_USER;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;


    private WayfareUserDetails wayfareUserDetails;
    @Autowired
    private WayfareUserDetailService wayfareUserDetailsService;

    @Autowired
    private UserRepository userRepo;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/register", consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseObject wayfareRegister(@RequestBody RegisterRequest registerRequest){
        System.out.println("test");

        try{
            UserCreationDTO toInsert = new UserCreationDTO(registerRequest.username,  registerRequest.password, registerRequest.verifypassword, registerRequest.email, registerRequest.phoneNumber, ROLE_USER);
            wayfareUserDetailsService.registerUser(toInsert);
            return new ResponseObject(true,"bruh");//inserted.getUsername());
        }
        catch (IllegalArgumentException e){
            return new ResponseObject(false, e.getMessage());
        }
        catch(DuplicateKeyException e){
            return new ResponseObject(false, "Username already exists");
        }
        catch (Exception e){
            return new ResponseObject(false, e.getMessage());
        }

    }
    @PostMapping("/login")
    public ResponseObject wayfareLogin(@RequestBody LoginRequest loginRequest) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);
        // ...
        return new ResponseObject(true, authenticationResponse);
    }

    public record LoginRequest(String username, String password) {
    }

    public record RegisterRequest(String username, String password, String verifypassword, String email, String phoneNumber, String roles) {
    }
}