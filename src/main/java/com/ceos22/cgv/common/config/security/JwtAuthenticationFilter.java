package com.ceos22.cgv.common.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String token = tokenProvider.getAccessToken(request);

        try {
            // SecurityContextHolder 채우기
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (tokenProvider.validateAccessToken(token)) {
                    var authentication = tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (AuthenticationException ex) {
            // 인증 관련 예외는 컨텍스트를 비우고 계속 진행 -> 보호된 엔드포인트에서 401/403 처리됨
            SecurityContextHolder.clearContext();
        } catch (Exception ex) {
            // 그 외 예외도 인증 실패로 간주하고 컨텍스트 클리어 후 계속 진행
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);

    }
}