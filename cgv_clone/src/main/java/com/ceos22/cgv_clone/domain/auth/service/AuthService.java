package com.ceos22.cgv_clone.domain.auth.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.auth.jwt.JwtTokenProvider;
import com.ceos22.cgv_clone.domain.member.dto.request.LoginRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.RefreshTokenRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.SignUpRequest;
import com.ceos22.cgv_clone.domain.member.dto.response.MemberInfoResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.TokenResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.Role;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoResponse signUp(SignUpRequest request) {

        log.info("회원가입 처리 시작 {}", request.email());

        if(memberRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .birthDate(request.birthDate())
                .role(Role.USER)
                .build();

        Member savedMember = memberRepository.save(member);

        log.info("회원가입 성공 {}", request.email());

        return MemberInfoResponse.from(savedMember);
    }

    public TokenResponse login(LoginRequest request) {
        log.info("로그인 처리 시작 {}", request.email());

        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        log.info("로그인 처리 완료 {}", request.email());

        return TokenResponse.of(
                accessToken,
                refreshToken
        );
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String userId = jwtTokenProvider.getTokenUserId(refreshToken);
        Member member = memberRepository.findById(Long.parseLong(userId))
                .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.createAccessToken(member);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member);

        return TokenResponse.of(
                newAccessToken,
                newRefreshToken
        );
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
