package com.ceos22.cgv_clone.domain.member.dto.response;

import com.ceos22.cgv_clone.domain.member.entity.Member;
import com.ceos22.cgv_clone.domain.member.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record MemberInfoResponse(
        @JsonProperty("member_id")
        Long memberId,

        String email,

        String name,

        @JsonProperty("birth_date")
        LocalDate birthDate,

        Role role
) {
    public static MemberInfoResponse from(Member member) {
        return new MemberInfoResponse(
                member.getMemberId(),
                member.getEmail().getValue(),
                member.getName().getValue(),
                member.getBirthDate().getValue(),
                member.getRole()
        );
    }
}
