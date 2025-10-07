package com.ceos22.cgv.config.security;

import com.ceos22.cgv.module.user.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {


    private Key key;
    private final String secret;
    private final UserService userService;
    private final long accessTokenValidityInMs;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-ms}") long accessTokenValidityMs,
            UserService userService) {
        this.secret = secret;
        this.accessTokenValidityInMs = accessTokenValidityMs;
        this.userService = userService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 요청에서 Bearer 토큰 추출
    public String getAccessToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        if (header == null || header.isBlank()) return null;
        if (!header.startsWith("Bearer "))  return null;

        return header.substring("Bearer ".length()).trim();
    }

    // Access Token 생성
    public String createAccessToken(Long userId, Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("auth", authorities)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(accessTokenValidityInMs)))
                .signWith((SecretKey) key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰에서 userId 추출
    public String getTokenUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // token에서 Authentication 생성 (SecurityContext에 넣을 객체)
    public Authentication getAuthentication(String token) {

        String _userId = getTokenUserId(token);
        Long userId = Long.valueOf(_userId);
        UserDetails userDetails = userService.loadUserByUserId(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }


    public boolean validateAccessToken(String token) {
        try{
            Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) { // 만료
            return false;
        } catch (Exception e) { // 서명 불일치, 형식 오류
            return false;
        }
    }

}