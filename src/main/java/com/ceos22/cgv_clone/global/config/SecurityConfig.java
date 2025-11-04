package com.ceos22.cgv_clone.global.config;

import com.ceos22.cgv_clone.global.security.JwtAuthenticationFilter;
import com.ceos22.cgv_clone.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        // cors
        http
                .cors(Customizer.withDefaults());

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // session 방식 disable
        http
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 설정
        http.authorizeHttpRequests(auth -> auth
                // Preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers("/", "/index.html", "/favicon.ico", "/error").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                // Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // 로그인/회원가입/공개 API
                .requestMatchers("/api/login", "/api/signup", "/api/members/**").permitAll()

                // 그 외
                .anyRequest().authenticated()
        );
        // STATELESS 방식 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // JWT 필터 적용
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);

        return source;
    }
}
