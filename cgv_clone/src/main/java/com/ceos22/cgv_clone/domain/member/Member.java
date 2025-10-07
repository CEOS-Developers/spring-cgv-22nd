package com.ceos22.cgv_clone.domain.member;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "member",
        uniqueConstraints = @UniqueConstraint(name = "uk_member_login_id", columnNames = "login_id")
)
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable=false, length=50)
    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length=30, unique = true)
    private String loginId;

    @Column(nullable = false, length=255)
    private String password;

    public Member(String name, int age, Gender gender, String loginId, String password) {
        this.name = name; this.age = age; this.gender = gender; this.loginId = loginId; this.password = password;
    }

}