package com.ceos22.cgv.module.user.dto;

import com.ceos22.cgv.module.user.domain.User;
import com.ceos22.cgv.util.UserGrade;
import com.ceos22.cgv.util.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;
    private final com.ceos22.cgv.util.UserGrade grade;
    private final com.ceos22.cgv.util.UserRole role;

    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.phone = user.getPhone();
        this.grade = user.getGrade();
        this.role = user.getRole();
        this.password = user.getPassword();
        this.authorities = java.util.List.of(
                new org.springframework.security.core.authority.SimpleGrantedAuthority(role.name())
        );
    }


    @Override
    public String getUsername() { //id를 사용
        return String.valueOf(userId);
    }
    @Override public String getPassword() { return password; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
