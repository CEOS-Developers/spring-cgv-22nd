package com.ceos22.springcgv.config.jwt;

import com.ceos22.springcgv.config.CustomUserDetails;
import com.ceos22.springcgv.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Authorization 헤더에서 토큰 추출
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.debug("No JWT token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 접두사 제거
        String token = authorization.substring(7);

        // 토큰 만료 여부 확인
        if (jwtUtil.isExpired(token)) {
            log.warn("JWT token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 userId, username, role 추출
        Long userId = jwtUtil.getUserId(token);
        String username = jwtUtil.getUsername(token);
        String roleStr = jwtUtil.getRole(token);

        log.info("JWT Filter - userId: {}, username: {}, role: {}", userId, username, roleStr);

        // String을 Role enum으로 변환
        User.Role role = User.Role.valueOf(roleStr);

        // User 엔티티 생성 (실제 DB 조회 없이 토큰 정보로 생성)
        User user = User.builder()
                .id(userId)
                .username(username)
                .password("temp") // 임시 비밀번호 (검증용이 아니므로)
                .role(role)
                .build();

        // CustomUserDetails 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                customUserDetails,
                null,
                customUserDetails.getAuthorities()
        );

        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Authentication set in SecurityContext for user: {}", username);

        filterChain.doFilter(request, response);
    }
}