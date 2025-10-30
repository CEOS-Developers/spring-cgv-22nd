package com.ceos22.cgv_clone.domain.member.entity;

import com.ceos22.cgv_clone.common.entity.BaseEntity;
import com.ceos22.cgv_clone.domain.member.vo.BirthDate;
import com.ceos22.cgv_clone.domain.member.vo.Email;
import com.ceos22.cgv_clone.domain.member.vo.MemberName;
import com.ceos22.cgv_clone.domain.member.vo.Password;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private MemberName name;

    @Embedded
    private BirthDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Member(Email email, Password password, MemberName name, BirthDate birthDate, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.role = role;
    }

    public static Member of(
            String email,
            String rawPassword,
            String name,
            LocalDate birthDate,
            PasswordEncoder passwordEncoder
    ) {
        return new Member(
                Email.from(email),
                Password.from(rawPassword, passwordEncoder),
                MemberName.from(name),
                BirthDate.from(birthDate),
                Role.USER
        );
    }

    public boolean matchesPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return this.password.matches(rawPassword, passwordEncoder);
    }

    // UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return String.valueOf(password);
    }

    @Override
    public String getUsername() {
        return String.valueOf(memberId);
    }
}
