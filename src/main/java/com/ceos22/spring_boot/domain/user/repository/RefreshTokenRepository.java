package com.ceos22.spring_boot.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceos22.spring_boot.entity.RefreshToken;
import com.ceos22.spring_boot.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByUser(User user);
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
	void deleteByUser(User user);
}
