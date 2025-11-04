package com.ceos22.cgv_clone.domains.member.service;

import com.ceos22.cgv_clone.domains.member.domain.Gender;
import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;


/**
 * 도메인 객체가 죔
 */
public record CreateMemberCommand(
        String name,
        int age,
        Gender gender,
        String loginId,
        String password
) {

    public MemberEntity toEntity() {
        return new MemberEntity(
                name,
                age,
                gender,
                loginId,
                password
        );
    }
}
