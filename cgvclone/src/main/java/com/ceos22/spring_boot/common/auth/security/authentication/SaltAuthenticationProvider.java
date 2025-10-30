package com.ceos22.spring_boot.common.auth.security.authentication;

import com.ceos22.spring_boot.common.auth.security.principal.CustomUserPrincipal;
import com.ceos22.spring_boot.common.auth.security.password.PasswordSaltService;
import com.ceos22.spring_boot.common.exception.AuthFailureException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import com.ceos22.spring_boot.domain.user.User;
import com.ceos22.spring_boot.domain.user.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaltAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository users;
    private final PasswordSaltService saltService;

    public SaltAuthenticationProvider(UserRepository users, PasswordSaltService saltService) {
        this.users = users; this.saltService = saltService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal());
        String rawPassword = String.valueOf(authentication.getCredentials());

        User user = users.findByUsername(username)
                .orElseThrow(() ->
                        new AuthFailureException(ErrorStatus.USER_NOT_FOUND, "존재하지 않는 사용자입니다.")
                );

        if (!saltService.matches(rawPassword, user.getSalt(), user.getPasswordHash())) {
            throw new AuthFailureException(ErrorStatus.INVALID_CREDENTIALS, "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        var authorities = List.<GrantedAuthority>of(() -> "ROLE_USER");
        var principal = new CustomUserPrincipal(
                user.getUserId(), user.getPublicId(), user.getUsername(),
                user.getPasswordHash(), user.getName(), user.getEmail(), authorities
        );
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    @Override public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
