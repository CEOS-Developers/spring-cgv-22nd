package com.ceos22.cgv_clone.domain.member.service;

import com.ceos22.cgv_clone.domain.member.dto.SignUpRequestDto;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.global.apiPayload.code.error.ErrorCode;
import com.ceos22.cgv_clone.global.apiPayload.exception.CustomException;
import com.ceos22.cgv_clone.global.security.JwtTokenProvider;
import com.ceos22.cgv_clone.global.security.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member signup(SignUpRequestDto request) {

        log.debug("Signup 시도: {}", request.getEmail());

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(request.getEmail())) {
            log.warn("Signup 실패: 이메일 중복. email: {}", request.getEmail());
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.create(
                request.getEmail(),
                encodedPassword,
                request.getNickname()
        );

        log.info("Member 생성. email: {}, memberId: {}", member.getEmail(), member.getId());

        return memberRepository.save(member);
    }

    @Transactional
    public TokenDto login(String email, String password) {

        log.debug("Login 시도: {}", email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login 실패: 이메일 없음. email: {}", email);
                    return new CustomException(ErrorCode.EMAIL_NOT_FOUND);
                });

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.warn("Login 실패: 비밀번호 불일치. email: {}", email);
            throw new CustomException(ErrorCode.PASSWORD_WRONG);
        }

        // Authentication 객체 생성
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getDescription()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getEmail(), null, authorities);

        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authentication);

        log.info("Login 성공. email: {}, memberId: {}", member.getEmail(), member.getId());

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

}
