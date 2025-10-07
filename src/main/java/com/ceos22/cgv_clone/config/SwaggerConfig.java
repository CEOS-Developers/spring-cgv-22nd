package com.ceos22.cgv_clone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition( // Swagger/OpenAPI 문서의 기본 정보 설정(제목/설명/버전)
        info = @Info(title = "CGV Clone API",
                description = "CGV Clone Project API 문서",
                version = "v1")
)
@SecurityScheme( // API 보안 스키마 정의
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}