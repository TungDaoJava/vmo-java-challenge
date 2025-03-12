package com.vmogroup.java_challange;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableMongock
public class JavaChallangeApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaChallangeApplication.class, args);
	}
}
