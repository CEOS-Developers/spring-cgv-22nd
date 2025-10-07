package com.ceos22.cgv.module.user.repository;

import com.ceos22.cgv.module.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);
}
