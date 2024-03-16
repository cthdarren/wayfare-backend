package com.wayfare.backend.security;

import com.wayfare.backend.exception.FormatException;
import com.wayfare.backend.helper.Mapper;
import com.wayfare.backend.model.User;
import com.wayfare.backend.model.UserCreationDTO;
import com.wayfare.backend.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

//This service provides all the functionality to view and manipulate data going into and out of the database
@Service
public class WayfareUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Username doesn't exist");
        return new WayfareUserDetails(user);
    }

    public void registerUser(UserCreationDTO userCreationDto) throws FormatException{
        userCreationDto.validate();
        if (userRepo.existsByUsername(userCreationDto.getUsername()))
            userCreationDto.addErrors("Username already exists");
        if (userRepo.existsByEmail(userCreationDto.getEmail()))
            userCreationDto.addErrors("Email already exists");
        if (userCreationDto.hasErrors())
            throw new FormatException(userCreationDto.getErrors());
        userRepo.save(new Mapper().toUser(userCreationDto));
    }
}
