package com.ceos22.cgv_clone.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component// 스프링 빈으로 등록
@RequiredArgsConstructor // final 필드에 대해 생성자 자동 주입
public class TokenProvider implements InitializingBean {
    private Key key; //JWT signature 검증을 위한 키

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long tokenValidTime;

    // key 생성(초기화)
    @Override
    public void afterPropertiesSet() {
        //Base64로 decode 후 kEY 객체 생성
        System.out.println(">> secret = " + secret); // 디버깅
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 헤더에서 토큰 꺼내기
    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); //"Bearer " 이후 문자열 때문에 7로 설정
        }
        return null;
    }

    // 토큰 생성
    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidTime);

        return Jwts.builder()
                .setSubject(authentication.getName()) // username 저장
                .claim("auth", authorities) // 권한 정보
                .setExpiration(validity)    // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰에서 username 꺼내기
    public String getTokenUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // username 꺼냄
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(getTokenUsername(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }

    // 토큰 유효성 검증
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 JWT 토큰입니다.");
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("유효하지 않은 JWT 토큰입니다.");
        }
        return false;
    }
}
