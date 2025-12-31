package com.ethical.aims.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ethical.aims"})
@EntityScan(basePackages = {"com.ethical.aims.entity"})
public class AimsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AimsApplication.class, args);
	}

}
