package com.ceos22.cgv_clone.service.member;

import com.ceos22.cgv_clone.domain.dto.Member;
import com.ceos22.cgv_clone.domain.member.MemberEntity;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    /** 회원 단건 조회 */
    public Member getById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId));
        return Member.from(member);
    }

    /** 회원 아이디 조회 */
    public Member getByLoginIdOrThrow(String loginId) {
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + loginId));
        return Member.from(member);
    }

}
