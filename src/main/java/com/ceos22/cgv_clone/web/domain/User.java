package com.ceos22.cgv_clone.web.domain;

import com.ceos22.cgv_clone.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @UuidGenerator
    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false,length = 20, unique = true)
    private String nickName;

    @Column(nullable = false,length = 20)
    private String name;

    @Column(nullable = false, unique = true,length = 50)
    private String email;

    private String password;

    @Column(length = 50,name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;
}
