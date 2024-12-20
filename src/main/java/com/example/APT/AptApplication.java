package com.example.APT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AptApplication {

	public static void main(String[] args) {
		SpringApplication.run(AptApplication.class, args);
	}

}
