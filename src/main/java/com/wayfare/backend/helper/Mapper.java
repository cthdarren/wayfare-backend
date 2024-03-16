package com.wayfare.backend.helper;

import com.wayfare.backend.model.User;
import com.wayfare.backend.model.UserCreationDTO;

public class Mapper {
    public User toUser(UserCreationDTO userCreationDTO)
    {
        return new User(userCreationDTO.getUsername(), userCreationDTO.getPlainPassword(), userCreationDTO.getEmail(), userCreationDTO.getPhoneNumber(), "USER");
    }
}
