package com.ceos22.cgv_clone.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { //OncePerRequestFilter: spring security가 제공하는 필터. HTTP 요청 1번당 무조건 1번 실행

    private final TokenProvider tokenProvider; // 토큰 추출, 검증, Authentication 객체 생성을 위함.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 토큰 추출
        String token = tokenProvider.getAccessToken(request);

        // 토큰 유효성 검사
        if (token != null && tokenProvider.validateAccessToken(token)) {
            // 토큰에서 Authentication 객체 복원
            Authentication authentication = tokenProvider.getAuthentication(token);
            // SecurityContext 에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}
