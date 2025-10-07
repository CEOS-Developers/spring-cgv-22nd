package com.ceos22.spring_boot.global.security.jwt.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ceos22.spring_boot.domain.user.repository.UserRepository;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.global.security.jwt.JwtValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtValidator jwtValidator;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(JwtValidator jwtValidator, UserRepository userRepository) {
		this.jwtValidator = jwtValidator;
		this.userRepository = userRepository;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().equals("/auth/reissue");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = jwtValidator.extractAccessTokenFromCookie(request);

		if(token != null && jwtValidator.validateToken(token)){
			Long userId = jwtValidator.getUserIdFromToken(token);
			User user = userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole())));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}
}
