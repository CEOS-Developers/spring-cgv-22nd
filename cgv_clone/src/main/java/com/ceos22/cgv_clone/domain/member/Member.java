package com.ceos22.cgv_clone.domain.member;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable=false, length=50)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length=30)
    private String phoneNum;

    public Member(String name, Integer age, Gender gender, String phoneNum) {
        this.name = name; this.age = age; this.gender = gender; this.phoneNum = phoneNum;
    }

}