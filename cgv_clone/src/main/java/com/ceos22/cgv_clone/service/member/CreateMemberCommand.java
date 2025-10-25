package com.ceos22.cgv_clone.service.member;

import com.ceos22.cgv_clone.domain.dibsOn.FavoriteMovieEntity;
import com.ceos22.cgv_clone.domain.dto.FavoriteMovie;
import com.ceos22.cgv_clone.domain.dto.Member;
import com.ceos22.cgv_clone.domain.member.Gender;
import com.ceos22.cgv_clone.domain.member.MemberEntity;

import java.util.Optional;

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
