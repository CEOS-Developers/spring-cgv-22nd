package com.ceos22.cgv_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CgvCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CgvCloneApplication.class, args);
	}

}
