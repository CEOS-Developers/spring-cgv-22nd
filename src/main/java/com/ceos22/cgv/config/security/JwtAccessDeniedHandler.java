package com.ceos22.cgv.config.security;

import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    @Override
    public void handle(
            jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response,
            org.springframework.security.access.AccessDeniedException accessDeniedException) throws java.io.IOException {

        response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        var body = java.util.Map.of(
                "code", "403 FORBIDDEN",
                "message", "접근 권한이 없습니다.",
                "statusCode", 403
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
