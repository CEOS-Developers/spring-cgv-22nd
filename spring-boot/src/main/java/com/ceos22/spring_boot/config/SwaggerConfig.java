package com.ceos22.spring_boot.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springBootAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CGV API")
                        .description("CGV 클론코딩 API 명세서")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("윤지")
                                .url("https://github.com/yooniicode"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("ERD")
                        .url("https://dbdiagram.io/d/CGVDB-68cb6db55779bb7265fdfa06"));
    }
}
