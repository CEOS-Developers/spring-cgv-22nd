package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token extends BaseEntity {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "refresh_token", nullable = false)
	private String refreshToken;

	@Column(name = "expiry_date", nullable = false)
	private LocalDateTime expiryDate;

	@Builder(access = PRIVATE)
	private Token(User user, String refreshToken, LocalDateTime expiryDate) {
		this.user = user;
		this.refreshToken = refreshToken;
		this.expiryDate = expiryDate;
	}

}
