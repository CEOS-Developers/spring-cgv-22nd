package com.ceos22.spring_boot.common.auth.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (log.isDebugEnabled()) {
            log.debug("Authorization Header = {}", request.getHeader("Authorization"));
            log.debug("Parsed token = {}", token);
        }

        if (token != null && tokenProvider.validate(token)) {
            var authentication = tokenProvider.getAuthentication(token);
            log.info("Authenticated user: {}", authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("JWT validation failed or token missing");
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (!token.isBlank()) {
                return token;
            }
        }
        return null;
    }
}
