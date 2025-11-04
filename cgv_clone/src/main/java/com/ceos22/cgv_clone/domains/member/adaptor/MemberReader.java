package com.ceos22.cgv_clone.domains.member.adaptor;

import com.ceos22.cgv_clone.domains.member.dto.Member;
import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;
import com.ceos22.cgv_clone.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    /** 회원 아이디 존재 여부 조회 */
    public Member getByLoginIdOrThrow(String loginId) {
        MemberEntity member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + loginId));
        return Member.from(member);
    }

    /** 회원 아이디 중복 여부 조회 */
    public boolean getByLoginId(String loginId) {
        Optional<MemberEntity> member = memberRepository.findByLoginId(loginId);
        if (!member.isPresent()) {
            return true; // 가입 가능
        } else {
            return false; // 가입 불가능
        }
    }

}
