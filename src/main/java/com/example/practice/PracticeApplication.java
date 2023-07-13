package com.example.practice;

import com.example.practice.controllers.CustomerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracticeApplication {
	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);
	public static void main(String[] args) {
		LOG.info("PracticeApplication start");
		SpringApplication.run(PracticeApplication.class, args);
	}

}
