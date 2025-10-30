package com.ceos22.spring_boot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserId(long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByPublicId(UUID publicId);
    Optional<User> findByUsername(String username);
}
