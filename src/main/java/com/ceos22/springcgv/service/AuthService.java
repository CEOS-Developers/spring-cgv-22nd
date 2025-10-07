package com.ceos22.springcgv.service;


import com.ceos22.springcgv.config.jwt.JWTUtil;
import com.ceos22.springcgv.domain.User;
import com.ceos22.springcgv.dto.LoginRequestDto;
import com.ceos22.springcgv.dto.SignUpRequestDto;
import com.ceos22.springcgv.dto.TokenResponseDto;
import com.ceos22.springcgv.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;


    public void signUp(SignUpRequestDto req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByNickname(req.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .nickname(req.getNickname())
                .build();
        userRepository.save(user);
    }

}