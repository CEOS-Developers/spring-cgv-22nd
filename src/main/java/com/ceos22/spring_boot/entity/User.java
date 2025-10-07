package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ceos22.spring_boot.entity.enums.Gender;
import com.ceos22.spring_boot.entity.enums.Rank;
import com.ceos22.spring_boot.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Column(nullable = false)
	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Rank rank;

	@Column(name = "provider_id", nullable = false)
	private String providerId;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "birth_date")
	private LocalDate birthDate;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Builder(access = PRIVATE)
	private User(String nickname, Role role, Rank rank, String providerId, Boolean isDeleted, LocalDate birthDate, Gender gender){
		this.nickname = nickname;
		this.role = role;
		this.rank = rank;
		this.providerId = providerId;
		this.isDeleted = isDeleted;
		this.birthDate = birthDate;
		this.gender = gender;
	}

	public static User createTmpUser(String kakaoId, String nickname){
		return User.builder()
			.providerId(kakaoId)
			.nickname(nickname)
			.rank(Rank.NORMAL)
			.role(Role.NORMAL_USER)
			.isDeleted(false)
			.birthDate(null)
			.gender(null)
			.build();
	}
}
