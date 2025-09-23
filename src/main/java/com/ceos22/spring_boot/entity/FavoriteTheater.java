package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_theater")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteTheater extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false)
	private Theater theater;

	@Builder(access = PRIVATE)
	private FavoriteTheater(User user, Theater theater) {
		this.user = user;
		this.theater = theater;
	}
}
