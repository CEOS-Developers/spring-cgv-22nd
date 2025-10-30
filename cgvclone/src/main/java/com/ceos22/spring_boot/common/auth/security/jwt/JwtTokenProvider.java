package com.ceos22.spring_boot.common.auth.security.jwt;

import com.ceos22.spring_boot.common.auth.properties.JwtProperties;
import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenProvider {

    private static final String AUTH_CLAIM = "auth";
    private final Key key;
    private final long validityMillis;
    private final UserRepository users;

    public JwtTokenProvider(JwtProperties props, UserRepository users) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        this.validityMillis = props.accessTokenValidity().toMillis();
        this.users = users;
    }

    public String issueToken(Authentication authentication) {
        var now = new Date();
        var expiry = new Date(now.getTime() + validityMillis);
        var authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        Object principal = authentication.getPrincipal();

        String subject;
        if (principal instanceof CustomUserPrincipal customPrincipal) {
            subject = customPrincipal.getPublicId().toString();
        } else {
            subject = authentication.getName();
        }

        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTH_CLAIM, authorities)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String subject = claims.getSubject();
        UUID publicId;
        try {
            publicId = UUID.fromString(subject);
        } catch (IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN, "잘못된 토큰 형식입니다: " + subject);
        }

        String authStr = claims.get(AUTH_CLAIM, String.class);
        var authorities = Optional.ofNullable(authStr)
                .stream()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User u = users.findByPublicId(publicId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST, "사용자를 찾을 수 없습니다: " + publicId));


        var principal = new CustomUserPrincipal(
                u.getUserId(),
                u.getPublicId(),
                u.getUsername(),
                u.getPasswordHash(),
                u.getName(),
                u.getEmail(),
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("JWT expired at {}", e.getClaims().getExpiration());
            throw new GeneralException(ErrorStatus.TOKEN_EXPIRED, "토큰이 만료되었습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new GeneralException(ErrorStatus.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }
    }


    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    public long getExpiration(String token) {
        Claims claims = parseClaims(token);
        Date expiration = claims.getExpiration();
        return (expiration.getTime() - System.currentTimeMillis()) / 1000L;
    }

}
