package com.wayfare.backend.repository;

import com.wayfare.backend.model.Role;
import com.wayfare.backend.model.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleEnum role);
}
