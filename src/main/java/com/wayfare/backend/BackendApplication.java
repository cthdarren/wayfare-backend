package com.wayfare.backend;

import com.wayfare.backend.model.GroceryItem;
import com.wayfare.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@SpringBootApplication
@EnableMongoRepositories
public class BackendApplication {

	@Autowired
	ItemRepository groceryItemRepo;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}


}