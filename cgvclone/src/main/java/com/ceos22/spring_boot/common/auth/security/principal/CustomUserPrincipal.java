package com.ceos22.spring_boot.common.auth.security.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class CustomUserPrincipal implements UserDetails {
    private final Long userId;
    private final UUID publicId;
    private final String username;
    private final String passwordHash;
    private final String name;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserPrincipal(Long userId, UUID publicId, String username, String passwordHash,
                               String name, String email,
                               Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.publicId = publicId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.name = name;
        this.email = email;
        this.authorities = authorities == null ? List.of() : authorities;
    }

    public Long getUserId() { return userId; }
    public UUID getPublicId() { return publicId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
