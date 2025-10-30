package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.security.CustomUserDetailsService;
import com.ceos22.cgv_clone.global.security.dto.RefreshTokenResponse;
import com.ceos22.cgv_clone.global.security.dto.TokenResponse;
import com.ceos22.cgv_clone.global.security.jwt.TokenProvider;
import com.ceos22.cgv_clone.global.service.RedisService;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.domain.enums.UserRole;
import com.ceos22.cgv_clone.web.dto.UserRequestDto;
import com.ceos22.cgv_clone.web.dto.UserResponseDto;
import com.ceos22.cgv_clone.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Transactional
    public UserResponseDto.UserSignUpResponseDto signup(UserRequestDto.UserSignUpDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new GeneralException(ErrorStatus.ALREADY_EXISTS_EMAIL);
        }

        if (!requestDto.getPassword().equals(requestDto.getConfirmPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_MISMATCH);
        }

        User user = User.builder()
                .email(requestDto.getEmail())
                .nickName(requestDto.getNickName())
                .name(requestDto.getName())
                .birthDate(requestDto.getBirth())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(UserRole.GENERAL)
                .build();

        userRepository.save(user);

        return UserResponseDto.UserSignUpResponseDto.of(user);
    }

    @Transactional
    public UserResponseDto.LoginTokenResponseDTO signIn(UserRequestDto.UserSignInDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()-> new GeneralException(ErrorStatus.EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new GeneralException(ErrorStatus.INVALID_PASSWORD);
        }

        Authentication authentication = createAuthenticationFromUser(user);
        TokenResponse tokenResponse = tokenProvider.createToken(user.getId(), authentication);
        String key = REFRESH_TOKEN_PREFIX + user.getId();
        redisService.setValue(key,tokenResponse.refreshToken(), refreshExpirationTime);

        return UserResponseDto.LoginTokenResponseDTO.of(user, tokenResponse.accessToken());
    }

    public UserResponseDto.RecreateTokenResponseDto recreateToken(String token) {
        tokenProvider.validateToken(token);

        Long userId = Long.valueOf(tokenProvider.getUserIdFromToken(token));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Authentication authentication = createAuthenticationFromUser(user);

        String key = REFRESH_TOKEN_PREFIX + userId;
        String storedToken = (String) redisService.getValue(key);
        if (storedToken == null || !storedToken.equals(token)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        RefreshTokenResponse newTokenDto = tokenProvider.recreate(userId,authentication);
        redisService.setValue(key,newTokenDto.refreshToken(),refreshExpirationTime);

        return UserResponseDto.RecreateTokenResponseDto.of(userId,newTokenDto.accessToken(),newTokenDto.refreshToken());
    }

    private Authentication createAuthenticationFromUser(User user) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getId().toString());
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return user;
    }
}
