package com.wayfare.backend.repository;

import com.wayfare.backend.model.GroceryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
@RepositoryRestResource(collectionResourceRel = "groceries", path = "groceries")

public interface ItemRepository extends MongoRepository<GroceryItem, String> {
    List<GroceryItem> findByName(@Param("name") String name);
}