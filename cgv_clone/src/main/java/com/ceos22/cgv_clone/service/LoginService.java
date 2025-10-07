package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.LoginReq;
import com.ceos22.cgv_clone.domain.dto.LoginRes;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.repository.MemberRepository;
import com.ceos22.cgv_clone.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public LoginRes login(LoginReq req) {
        // 아이디 확인
        Member m = memberRepository.findByLoginId(req.loginId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        // 비밀번호 확인
        if (!passwordEncoder.matches(req.password(), m.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        UserDetails user = new User(
                m.getLoginId(),
                m.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );

        // 액세스 토큰 발급
        String accessToken = tokenProvider.createAccessToken(m.getId(), auth);

        return new LoginRes(accessToken);
    }
}
