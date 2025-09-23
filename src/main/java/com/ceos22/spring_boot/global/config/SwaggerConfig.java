package com.ceos22.spring_boot.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI(){
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("Authorization", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.in(SecurityScheme.In.HEADER)
					.bearerFormat("JWT")
				)
			)
			.addSecurityItem(new SecurityRequirement().addList("Authorization"))
			.info(new Info()
				.title("CGV API")
				.description("CGV API 명세서")
				.version("1.0.0")
			);
	}
}
