package com.ceos22.cgv_clone.domain.member.entity;

import com.ceos22.cgv_clone.domain.common.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    // 가입 시 이메일 @앞부분으로 자동 설정
    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING) // Enum 타입을 문자열로 DB에 저장
    @Column(nullable = false)
    private Role role;

    public static Member create(String email, String password, String nickname) {
        Member member = new Member();
        member.email = email;
        member.password = password;
        member.nickname = nickname;
        member.role = Role.USER;    // 기본은 USER로 생성됨

        return member;
    }
}
