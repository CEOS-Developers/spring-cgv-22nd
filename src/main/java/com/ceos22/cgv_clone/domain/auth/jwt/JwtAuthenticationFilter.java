package com.ceos22.cgv_clone.domain.auth.jwt;

import com.ceos22.cgv_clone.domain.auth.jwt.error.BlacklistedTokenException;
import com.ceos22.cgv_clone.domain.auth.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    // Filter에서 발생하는 Error를 GlobalExceptionHandler으로 위임
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final RedisService redisService;

    private static final String BlACKLIST_PREFIX = "BL:";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = jwtTokenProvider.getAccessToken(request);

            if(token != null && !token.isEmpty()) {
                String blackListKey = BlACKLIST_PREFIX + token;
                if (redisService.hasKey(blackListKey)) {
                    SecurityContextHolder.clearContext();
                    throw new BlacklistedTokenException();
                }

                jwtTokenProvider.validateToken(token);

                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
