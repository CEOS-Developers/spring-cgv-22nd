package com.ceos22.spring_boot.common.auth.security.jwt;

import com.ceos22.spring_boot.common.exception.AuthFailureException;
import com.ceos22.spring_boot.common.response.ApiResponse;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse res,
                         AuthenticationException ex) throws IOException {
        ErrorStatus status;
        String message = ex.getMessage();

        if (ex instanceof AuthFailureException afe) {
            status = afe.getErrorStatus();
        } else {
            status = ErrorStatus._UNAUTHORIZED;
        }

        var responseEntity = ApiResponse.onFailure(status);

        res.setStatus(status.getHttpStatus().value());
        res.setContentType("application/json;charset=UTF-8");
        om.writeValue(res.getWriter(), responseEntity.getBody());
    }
}
