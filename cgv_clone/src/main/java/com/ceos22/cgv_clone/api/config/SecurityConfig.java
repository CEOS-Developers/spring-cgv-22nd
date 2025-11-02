package com.ceos22.cgv_clone.api.config;

import com.ceos22.cgv_clone.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    private static final String[] SWAGGER = {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 가장 많이 쓰이는 BCrypt 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // (1) csrf
                .csrf(AbstractHttpConfigurer::disable)

                // (2) 세션
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // (3) 인증 / 인가
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers(SWAGGER).permitAll() // Swagger 허용
                        .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll() // 로그인/회원가입
                        .requestMatchers("/api/reservation/**",
                                "/api/favorites/movies/*/*/toggle",
                                "/api/favorites/cinemas/*/*/toggle",
                                "/api/movies/**",
                                "/api/cinemas/**,",
                                "/api/payments/**").permitAll()
                        .anyRequest().authenticated() // 나머지 인증
                )

                // (4) JWT 필터 위치
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
