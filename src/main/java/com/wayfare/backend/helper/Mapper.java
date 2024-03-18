package com.wayfare.backend.helper;

import com.wayfare.backend.model.User;
import com.wayfare.backend.model.UserCreationDTO;

import java.time.Instant;

import static com.wayfare.backend.model.RoleEnum.ROLE_USER;

public class Mapper {
    public User toUser(UserCreationDTO userCreationDTO)
    {
        return new User(userCreationDTO.getUsername(), userCreationDTO.getFirstName(), userCreationDTO.getLastName(), userCreationDTO.getPlainPassword(), userCreationDTO.getEmail(), userCreationDTO.getPhoneNumber(), userCreationDTO.getRole(),false, Instant.now(), Instant.now());
    }
}
