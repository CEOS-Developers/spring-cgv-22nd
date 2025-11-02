package com.ceos22.cgv_clone.domains.member.dto;

import com.ceos22.cgv_clone.domains.member.domain.Gender;
import com.ceos22.cgv_clone.domains.member.domain.MemberEntity;

/**
 * 도메인 객체
 * @param id
 * @param loginId
 * @param name
 * @param age
 * @param gender
 */
public record Member(
        Long id,
        String loginId,
        String name,
        int age,
        Gender gender
) {
    public static Member from(MemberEntity m) {
        return new Member(
                m.getId(),
                m.getLoginId(),
                m.getName(),
                m.getAge(),
                m.getGender()
        );
    }
}
