package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ceos22.spring_boot.entity.enums.Gender;
import com.ceos22.spring_boot.entity.enums.Rank;

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
	private String email;

	@Column(nullable = false)
	private String nickname;

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
	private User(String email, String nickname, Rank rank, String providerId, Boolean isDeleted, LocalDate birthDate, Gender gender){
		this.email = email;
		this.nickname = nickname;
		this.rank = rank;
		this.providerId = providerId;
		this.isDeleted = isDeleted;
		this.birthDate = birthDate;
		this.gender = gender;
	}
}
