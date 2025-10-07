package com.ceos22.cgv_clone.domain.member.service;

import com.ceos22.cgv_clone.domain.member.dto.SignUpRequestDto;
import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.repository.MemberRepository;
import com.ceos22.cgv_clone.global.security.JwtTokenProvider;
import com.ceos22.cgv_clone.global.security.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member signup(SignUpRequestDto requestDto) {

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.create(
                requestDto.getEmail(),
                encodedPassword,
                requestDto.getNickname()
        );

        return memberRepository.save(member);
    }

    public TokenDto login(String email, String password) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // Authentication 객체 생성
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getKey()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getEmail(), null, authorities);

        // 액세스 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authentication);

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

}
