package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.User;
import com.ceos22.cgv_clone.jwt.TokenProvider;
import com.ceos22.cgv_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    // 회원가입
    public User signup(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("이미 존재하는 사용자입니다.");
        }
        User user = User.create(username, passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    // 로그인
    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return tokenProvider.createAccessToken(authentication);
    }
}
