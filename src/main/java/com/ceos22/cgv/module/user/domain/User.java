package com.ceos22.cgv.module.user.domain;

import com.ceos22.cgv.common.domain.BaseEntity;
import com.ceos22.cgv.common.util.UserGrade;
import com.ceos22.cgv.common.util.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private UserGrade grade;

    @Column(name = "phone", length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
