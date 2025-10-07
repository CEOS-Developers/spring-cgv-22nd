package com.ceos22.spring_boot.domain.user;

import com.ceos22.spring_boot.common.auth.security.password.PasswordSaltService;
import com.ceos22.spring_boot.common.exception.GeneralException;
import com.ceos22.spring_boot.common.response.status.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository users;
    private final PasswordSaltService saltService;

    public UserService(UserRepository users, PasswordSaltService saltService) {
        this.users = users; this.saltService = saltService;
    }

    @Transactional
    public User register(String username, String rawPassword, String name, String email) {
        // 중복 확인 로직
        if (users.existsByUsername(username)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_USERNAME, "이미 존재하는 사용자명입니다: " + username);
        }
        if (users.existsByEmail(email)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_EMAIL, "이미 존재하는 이메일입니다: " + email);
        }

        // 솔트 + 해시 생성
        String salt = saltService.generateSaltBase64();
        String hash = saltService.hashBase64(rawPassword, salt);

        // 유저 생성
        User user = User.builder()
                .username(username)
                .salt(salt)
                .passwordHash(hash)
                .name(name)
                .email(email)
                .build();

        return users.save(user);
    }

}
