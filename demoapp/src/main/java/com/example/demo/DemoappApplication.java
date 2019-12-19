package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages ={"com.example.demo.services",
		"com.example.demo.repo", "com.example.demo.controllers", "com.example.demo.entities"})
@EnableJpaRepositories("com.example.demo.repo")
@EnableAutoConfiguration
public class DemoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoappApplication.class, args);
	}

}
