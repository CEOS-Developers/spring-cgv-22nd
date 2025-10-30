package com.ceos22.cgv_clone.domain.auth.service;

import com.ceos22.cgv_clone.common.error.CustomException;
import com.ceos22.cgv_clone.common.error.ErrorCode;
import com.ceos22.cgv_clone.domain.auth.jwt.JwtTokenProvider;
import com.ceos22.cgv_clone.domain.auth.jwt.error.JwtException;
import com.ceos22.cgv_clone.domain.member.dto.request.LoginRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.RefreshTokenRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.SignUpRequest;
import com.ceos22.cgv_clone.domain.member.dto.response.MemberInfoResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.TokenResponse;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.domain.member.vo.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    private static final String REFRESH_TOKEN_PREFIX = "RT:";
    private static final String BLACKLIST_PREFIX = "BL:";

    public MemberInfoResponse signUp(SignUpRequest request) {
        log.info("회원가입 처리 시작 {}", request.email());

        Email email = request.toEmail();
        validateDuplicateEmail(email);

        Member member = Member.of(
                request.email(),
                request.password(),
                request.name(),
                request.birthDate(),
                passwordEncoder
        );

        Member savedMember = memberRepository.save(member);

        log.info("회원가입 성공 {}", request.email());

        return MemberInfoResponse.from(savedMember);
    }

    public TokenResponse login(LoginRequest request) {
        log.info("로그인 처리 시작 {}", request.email());

        Email email = request.toEmail();
        Member member = findMemberByEmail(email);

        validatePassword(member, request.password());

        TokenResponse tokenResponse = createAndSaveTokens(member);

        log.info("로그인 처리 완료 {}", request.email());

        return tokenResponse;
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        jwtTokenProvider.validateToken(refreshToken);

        String userId = jwtTokenProvider.getTokenUserId(refreshToken);
        validateStoredRefreshToken(userId, refreshToken);

        Member member = findMemberById(Long.parseLong(userId));

        deleteRefreshToken(userId);

        return createAndSaveTokens(member);
    }

    public void logout(String accessToken) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validateAuthentication(authentication);

        String userId = jwtTokenProvider.getTokenUserId(accessToken);

        addTokenBlackList(accessToken);
        deleteRefreshToken(userId);

        SecurityContextHolder.clearContext();
    }

    // ================================================================================================================

    private Member findMemberByEmail(Email email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateDuplicateEmail(Email email) {
        if(memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private void validatePassword(Member member, String rawPassword) {
        if(!member.matchesPassword(rawPassword, passwordEncoder)) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    private TokenResponse createAndSaveTokens(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        saveRefreshToken(member.getMemberId(), refreshToken);

        return TokenResponse.of(accessToken, refreshToken);
    }

    private void saveRefreshToken(Long memberId, String refreshToken) {
        String redisKey = REFRESH_TOKEN_PREFIX + memberId;
        redisService.setValueWithExpiration(
                redisKey,
                refreshToken,
                7,
                TimeUnit.DAYS
        );
    }

    private void addTokenBlackList(String accessToken) {
        if(accessToken == null || accessToken.isEmpty()) {
            return;
        }

        try {
            long expiration = jwtTokenProvider.getExpiration(accessToken);
            String blackListKey = BLACKLIST_PREFIX + accessToken;
            redisService.setValueWithExpiration(
                    blackListKey,
                    "logout",
                    expiration,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception e) {
            log.error("Access Token 블랙리스트 등록 실패 : {}", e.getMessage());
        }
    }

    private void deleteRefreshToken(String userId) {
        String refreshTokenKey = REFRESH_TOKEN_PREFIX + userId;
        redisService.deleteValue(refreshTokenKey);
    }

    private void validateStoredRefreshToken(String userId, String refreshToken) {
        String redisKey = REFRESH_TOKEN_PREFIX + userId;
        String storedToken = redisService.getValue(redisKey);

        if(storedToken == null || !storedToken.equals(refreshToken)) {
            redisService.deleteValue(redisKey);
            throw new JwtException(ErrorCode.INVALID_TOKEN);
        }
    }
}
