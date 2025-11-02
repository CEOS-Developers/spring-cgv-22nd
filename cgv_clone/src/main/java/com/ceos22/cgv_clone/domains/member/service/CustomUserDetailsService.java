package com.ceos22.cgv_clone.domains.member.service;

import com.ceos22.cgv_clone.domains.member.dto.Member;
import com.ceos22.cgv_clone.domains.member.adaptor.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberReader memberReader;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberReader.getByLoginIdOrThrow(loginId);

        return User.builder()
                .username(member.loginId())   // username = loginId
                .build();
    }
}
