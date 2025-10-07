package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
