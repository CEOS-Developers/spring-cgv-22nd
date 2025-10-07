package com.ceos22.cgv.config.security;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    @Override
    public void commence(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            org.springframework.security.core.AuthenticationException authException) throws java.io.IOException {

        response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        var body = java.util.Map.of(
                "code", "401 UNAUTHORIZED",
                "message", "유효한 토큰이 없습니다.",
                "statusCode", 401
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
