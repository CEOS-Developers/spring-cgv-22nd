package com.ceos22.cgv_clone.global.security.jwt;

import com.ceos22.cgv_clone.global.security.dto.RefreshTokenResponse;
import com.ceos22.cgv_clone.global.security.dto.TokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    private final UserDetailsService userDetailsService;

    @Value("${jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long id, Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        Date now = new Date();
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("auth",authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshTOken(Long id) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setSubject(id.toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponse createToken(Long userId, Authentication authentication) {
        return TokenResponse.of(
                createAccessToken(userId,authentication),
                createRefreshTOken(userId)
        );
    }

    public RefreshTokenResponse recreate(Long userId, Authentication authentication) {
        String accessToken = createAccessToken(userId,authentication);
        String refreshToken = createRefreshTOken(userId);

        return RefreshTokenResponse.of(userId,accessToken,refreshToken);
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails =
                (UserDetails)
                        userDetailsService.loadUserByUsername(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException ex) {
            throw new IllegalArgumentException("Invalid JWT signature or token malformed: "+ ex.getMessage());
        } catch (ExpiredJwtException ex) {
            throw new IllegalArgumentException("Expired JWT token: "+ ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new IllegalArgumentException("Unsupported JWT token: "+ ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT token compact of null or empty: "+ ex.getMessage());
        }
    }
}
