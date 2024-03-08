package com.wayfare.backend.repository;

import com.wayfare.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


// collectionResourceRel refers to the name of the collection in mongoDB
// path refers to the REST API URL to be accessed from at e.g. path = users is at localhost:8080/users
@RepositoryRestResource(collectionResourceRel = "users", path = "users")

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByFirstName(@Param("firstName") String firstName);
}