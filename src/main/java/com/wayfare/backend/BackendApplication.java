package com.wayfare.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.LocalTime;

@SpringBootApplication
@EnableMongoRepositories

public class BackendApplication {

	public static void main(String[] args) {
		System.out.println(LocalTime.now());
		SpringApplication.run(BackendApplication.class, args);
	}
}