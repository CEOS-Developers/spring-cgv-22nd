package com.ceos22.cgv_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //BaseEntity의 created_at, updated_at을 위함.
public class CgvCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CgvCloneApplication.class, args);
	}

}