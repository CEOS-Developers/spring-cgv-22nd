package com.ceos22.cgv.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reservations/*/cancel").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/*/cancel").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/order").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/order/*").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/*/like").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/*/unlike").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cinemas/*/like").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cinemas/*/unlike").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
