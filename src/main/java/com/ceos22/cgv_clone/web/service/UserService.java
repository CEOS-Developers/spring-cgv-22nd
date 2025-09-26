package com.ceos22.cgv_clone.web.service;

import com.ceos22.cgv_clone.global.apiPayload.code.ErrorStatus;
import com.ceos22.cgv_clone.global.apiPayload.exception.GeneralException;
import com.ceos22.cgv_clone.global.security.TokenProvider;
import com.ceos22.cgv_clone.web.domain.User;
import com.ceos22.cgv_clone.web.dto.UserRequestDto;
import com.ceos22.cgv_clone.web.dto.UserResponseDto;
import com.ceos22.cgv_clone.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String accessToken = tokenProvider.createAccessToken(user.getId(), authentication);

        return UserResponseDto.LoginTokenResponseDTO.of(user, accessToken);
    }
}
