package com.ceos22.springcgv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringCgvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCgvApplication.class, args);
	}

}
