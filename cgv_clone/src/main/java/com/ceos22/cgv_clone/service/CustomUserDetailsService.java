package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음: " + loginId));

        return User.builder()
                .username(member.getLoginId())   // username = loginId
                .password(member.getPassword())  // BCrypt 암호화된 비밀번호여야 함
                .build();
    }
}
