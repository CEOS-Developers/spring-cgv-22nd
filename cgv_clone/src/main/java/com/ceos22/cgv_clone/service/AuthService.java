package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.SignUpReq;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.Role;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder

    @Transactional
    public void signUp(SignUpReq req) {
        // 이미 존재하는 loginId 체크
        if (memberRepository.findByLoginId(req.loginId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자");
        }

        // 새 회원 생성 (비밀번호는 반드시 암호화)
        Member m = new Member(
                req.name(),
                req.age(),
                req.gender(),
                req.loginId(),
                passwordEncoder.encode(req.password())
        );

        memberRepository.save(m);
    }
}