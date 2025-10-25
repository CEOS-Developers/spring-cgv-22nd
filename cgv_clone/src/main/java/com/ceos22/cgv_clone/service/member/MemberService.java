package com.ceos22.cgv_clone.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 멤버를 생성하는 도메인 서비스.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberReader memberReader;
    private final MemberSaver memberSaver;

    /** 회원 가입 */
    @Transactional
    public Long join(CreateMemberCommand member) {
        //validate

        return memberSaver.join(member);
    }

    /** 회원 권한 검증*/
    @Transactional
    public void validateUserExist(Long memberId) {
        memberReader.getById(memberId);
    }

}
