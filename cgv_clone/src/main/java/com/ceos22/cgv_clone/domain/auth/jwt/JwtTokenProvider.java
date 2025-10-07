package com.ceos22.cgv_clone.domain.auth.jwt;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

    @Value("${spring.jwt.secret}")
    private String secret;
    @Value("${spring.jwt.token.access-expiration-time}")
    private Long accessTokenExpireTime;
    @Value("${spring.jwt.token.refresh-expiration-time}")
    private Long refreshTokenExpireTime;
    private SecretKey key;

    private final UserDetailsService userDetailsService;

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String TOKEN_PREFIX = "Bearer ";

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION_HEADER);
        if(accessToken!=null && accessToken.startsWith(TOKEN_PREFIX)) {
            return accessToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public String createToken(Long id, Role role, Long expirationTime) {
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("auth", "ROLE_" + role.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    public String createAccessToken(Member member) {
        return createToken(member.getMemberId(), member.getRole(), accessTokenExpireTime);
    }

    public String createRefreshToken(Member member) {
        return createToken(member.getMemberId(), member.getRole(), refreshTokenExpireTime);
    }

    public String getTokenUserId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // userName(jwt에서 사용자를 구분하기 위해 사용) = memberId
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT 토큰 서명 검증에 실패했습니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 null이거나 빈 문자열입니다: {}", e.getMessage());
        }
        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            // 이런 저런 오류들이 발생해도 만료되어서 사용하지 못하는 것과 같으니까 그냥 다 만료인 것으로 간주해버리기
            return true;
        }
    }
}
