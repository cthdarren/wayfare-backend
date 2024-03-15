package com.wayfare.backend.security;

import com.wayfare.backend.model.User;
import com.wayfare.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    public void registerUser(User user) throws DuplicateKeyException{
        User newUser = user;
        userRepo.save(newUser);
    }
}
