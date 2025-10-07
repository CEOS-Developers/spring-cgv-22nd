package com.ceos22.spring_boot.global.security.jwt;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.ceos22.spring_boot.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access-token-validity}")
	private long ACCESS_TOKEN_VALIDITY_SECONDS;

	@Value("${jwt.refresh-token-validity}")
	private long REFRESH_TOKEN_VALIDITY_SECONDS;

	@Value("${jwt.cookie.domain}")
	private String cookieDomain;

	@Value("${jwt.cookie.secure}")
	private boolean isSecure;

	@Value("${jwt.cookie.same-site}")
	private String sameSite;

	public String createAccessToken(User user) {
		return Jwts.builder()
			.setSubject(String.valueOf(user.getId()))
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS))
			.signWith(SignatureAlgorithm.HS512, secret.getBytes())
			.compact();
	}

	public String createRefreshToken(User user) {
		return Jwts.builder()
			.setSubject(String.valueOf(user.getId()))
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_SECONDS))
			.signWith(SignatureAlgorithm.HS256, secret.getBytes())
			.compact();
	}

	public void addAccessTokenCookie(HttpServletResponse response, String token){
		ResponseCookie cookie = ResponseCookie.from("accessToken", token)
			.path("/")
			.httpOnly(true)
			.secure(isSecure)
			.maxAge(ACCESS_TOKEN_VALIDITY_SECONDS / 1000)
			.sameSite(sameSite)
			.domain(cookieDomain.isBlank() ? null : cookieDomain)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());

	}

	public void addRefreshTokenCookie(HttpServletResponse response, String token){
		ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
			.path("/")
			.httpOnly(true)
			.secure(isSecure)
			.maxAge(REFRESH_TOKEN_VALIDITY_SECONDS / 1000)
			.sameSite(sameSite)
			.domain(cookieDomain.isBlank() ? null : cookieDomain)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void deleteAccessTokenCookie(HttpServletResponse response){
		ResponseCookie cookie = ResponseCookie.from("accessToken", "")
			.path("/")
			.httpOnly(true)
			.secure(isSecure)
			.maxAge(0)
			.sameSite(sameSite)
			.domain(cookieDomain.isBlank() ? null : cookieDomain)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public void deleteRefreshTokenCookie(HttpServletResponse response){
		ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
			.path("/")
			.httpOnly(true)
			.secure(isSecure)
			.maxAge(0)
			.sameSite(sameSite)
			.domain(cookieDomain.isBlank() ? null : cookieDomain)
			.build();

		response.addHeader("Set-Cookie", cookie.toString());
	}

	public LocalDateTime getRefreshTokenExpiry(String token){
		Claims claims = Jwts.parser()
			.verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
			.build()
			.parseSignedClaims(token)
			.getPayload();

		return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
	}

}

