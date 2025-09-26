package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.GlobalExceptionHandler;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.dto.UserRequestDto;
import com.ceos22.cgv_clone.web.dto.UserResponseDto;
import com.ceos22.cgv_clone.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto.UserSignUpResponseDto signup(UserRequestDto.UserSignUpDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }

        String rawPassword = UUID.randomUUID().toString();

        User user = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(rawPassword))
                .build();

        userRepository.save(user);

        return UserResponseDto.UserSignUpResponseDto.of(user);
    }
}
