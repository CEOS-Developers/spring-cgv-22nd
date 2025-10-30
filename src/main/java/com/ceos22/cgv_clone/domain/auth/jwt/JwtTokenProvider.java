package com.ceos22.cgv_clone.domain.auth.jwt;

import com.ceos22.cgv_clone.domain.auth.jwt.error.ExpiredTokenException;
import com.ceos22.cgv_clone.domain.auth.jwt.error.InvalidTokenException;
import com.ceos22.cgv_clone.domain.auth.jwt.error.MalformedTokenException;
import com.ceos22.cgv_clone.domain.auth.jwt.error.UnsupportedTokenException;
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

    /**
     * 토큰에서 Authentication 객체 생성
     * 이후 credential은 사용하지 않아 null으로 생성
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getTokenUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedTokenException("지원되지 않는 토큰 형식입니다.");
        } catch (MalformedJwtException e) {
            throw new MalformedTokenException("잘못된 토큰 형식입니다.");
        } catch (JwtException e) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("토큰이 비어있습니다.");
        }
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

    public long getExpiration(String token) {
        Date expiration = getClaims(token).getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
