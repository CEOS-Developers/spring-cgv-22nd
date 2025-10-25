package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.Member;
import com.ceos22.cgv_clone.domain.dto.SignUpReq;
import com.ceos22.cgv_clone.domain.member.MemberEntity;
import com.ceos22.cgv_clone.repository.MemberRepository;
import com.ceos22.cgv_clone.service.member.CreateMemberCommand;
import com.ceos22.cgv_clone.service.member.MemberReader;
import com.ceos22.cgv_clone.service.member.MemberSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberReader memberReader;
    private final MemberSaver memberSaver;
    private final PasswordEncoder passwordEncoder; // BCryptPasswordEncoder

    @Transactional
    public void signUp(SignUpReq req) {
        // 이미 존재하는 loginId 체크
        Optional<Member> member = Optional.ofNullable(memberReader.getByLoginIdOrThrow(req.loginId()));
        if(member.isPresent()){
            throw new IllegalArgumentException("member alerady ");
        }

        // 새 회원 생성 (비밀번호는 반드시 암호화)
        CreateMemberCommand m = new CreateMemberCommand(
                req.name(),
                req.age(),
                req.gender(),
                req.loginId(),
                passwordEncoder.encode(req.password())
        );

        memberSaver.join(m);

    }
}