package com.ceos22.cgv.module.user.service;

import com.ceos22.cgv.config.security.TokenProvider;
import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.module.user.dto.AuthRequest;
import com.ceos22.cgv.module.user.dto.AuthResponse;
import com.ceos22.cgv.module.user.dto.SignupRequest;
import com.ceos22.cgv.module.user.dto.SignupResponse;
import com.ceos22.cgv.module.user.repository.UserRepository;
import com.ceos22.cgv.util.UserGrade;
import com.ceos22.cgv.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.nickname(), request.password()));

        Long userId = userRepository.findByNickname(request.nickname())
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + request.nickname()));

        String accessToken = tokenProvider.createAccessToken(userId, authentication);

        return new AuthResponse(accessToken);
    }

    @Transactional
    public SignupResponse signup(SignupRequest request){

        if (userRepository.findByNickname(request.nickname()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 userId 입니다.");
        }

        User user = User.builder()
                .email(request.email())
                .grade(UserGrade.NORMAL)
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .phone(request.phone())
                .role(UserRole.ROLE_USER)
                .build();

        User saved = userRepository.save(user);

        return SignupResponse.from(saved);
    }
}
