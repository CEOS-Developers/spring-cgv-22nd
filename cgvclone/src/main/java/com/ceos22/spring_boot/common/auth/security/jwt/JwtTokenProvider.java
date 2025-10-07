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

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    private static final String AUTH_CLAIM = "auth";
    private final Key key;
    private final long validityMillis;
    private final UserRepository users;

    public JwtTokenProvider(JwtProperties props, UserRepository users) {
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
        this.validityMillis = props.accessTokenValidityInSeconds() * 1000L;
        this.users = users;
    }

    public String issueToken(Authentication authentication) {
        var now = new Date();
        var expiry = new Date(now.getTime() + validityMillis);
        var authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTH_CLAIM, authorities)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();
        String authStr = claims.get(AUTH_CLAIM, String.class);
        var authorities = Optional.ofNullable(authStr)
                .stream()
                .flatMap(s -> Arrays.stream(s.split(",")))
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User u = users.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BAD_REQUEST, "사용자를 찾을 수 없습니다: " + username));

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
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }
}
