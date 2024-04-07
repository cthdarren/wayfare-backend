package com.wayfare.backend.repository;

import com.wayfare.backend.model.Role;
import com.wayfare.backend.model.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CurrencyRepository extends MongoRepository<Currency, String> {
    Optional<Role> findByName(RoleEnum role);
}
