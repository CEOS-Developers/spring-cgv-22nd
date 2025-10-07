package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.dto.MemberDto;
import com.ceos22.cgv_clone.domain.dto.MovieDto;
import com.ceos22.cgv_clone.domain.member.Member;
import com.ceos22.cgv_clone.domain.reservationMovie.Movie;
import com.ceos22.cgv_clone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /** 회원 가입 */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증 (loginId)
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /** 회원 단건 조회 */
    public MemberDto getById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId));
        return MemberDto.from(member);
    }

}
