package com.wayfare.backend.security.jwt;

import com.wayfare.backend.ResponseObject;
import com.wayfare.backend.controller.LoginController;
import com.wayfare.backend.model.RoleEnum;
import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userrepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userrepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userrepo = userrepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = new JwtService();
        this.authenticationManager = authenticationManager;
    }

    public ResponseObject register(LoginController.RegisterRequest request){
        User user = new User(request.username(), request.password(), request.email(), request.phoneNumber(), RoleEnum.ROLE_USER);

        user = userrepo.save(user);

        String token = jwtService.generateToken(user);

        return new ResponseObject(true, token);
    }

    public ResponseObject authenticate(LoginController.LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userrepo.findByUsername(request.username());
        String token = jwtService.generateToken(user);

        return new ResponseObject(true, token);
    }
}
