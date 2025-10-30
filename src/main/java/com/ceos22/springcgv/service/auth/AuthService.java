package com.ceos22.springcgv.service.auth;


import com.ceos22.springcgv.config.jwt.JWTUtil;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.dto.user.SignUpRequestDto;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import com.ceos22.springcgv.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;


    public void signUp(SignUpRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USER_DUPLICATE_ID);
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.USER_DUPLICATE_NICKNAME);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhoneNumber())
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);
    }

}