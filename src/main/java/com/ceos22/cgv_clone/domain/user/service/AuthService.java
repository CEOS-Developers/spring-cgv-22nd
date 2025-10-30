package com.ceos22.cgv_clone.domain.user.service;

import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.security.jwt.TokenProvider;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    // 회원가입
    public User signup(String username, String password) {
        log.debug("[SVC] signup start - username={}", username);
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new IllegalStateException("이미 존재하는 사용자입니다.");
            }

            User user = User.create(username, passwordEncoder.encode(password));
            User saved = userRepository.save(user);

            log.info("[SVC] 회원가입 완료 - username={}, id={}", username, saved.getId());
            return saved;

        }
        catch (IllegalStateException e) {
            log.warn("[SVC] 회원가입 실패(사용자 중복) - username={}, msg={}", username, e.getMessage());
            throw e; // warn으로 이미 처리됨
        }
        catch (Exception e) {
            log.error("[SVC] 회원가입 실패 - username={}", username, e);
            throw e;
        }
    }

    // 로그인
    public String login(String username, String password) {
        log.debug("[SVC] login start - username={}", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenProvider.createAccessToken(authentication);
            log.info("[SVC] 로그인 성공 - username={}", username);
            return token;

        }
        catch (org.springframework.security.core.AuthenticationException e) {
            log.warn("[SVC] 로그인 실패 - username={}", username);
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 로그인 처리 중 오류 - username={}", username, e);
            throw e;
        }
    }
}
