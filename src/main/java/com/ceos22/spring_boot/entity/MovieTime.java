package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@Table(name = "movie_time")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieTime extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "screen_id", nullable = false)
	private Screen screen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;

	@Builder(access = PRIVATE)
	private MovieTime(Screen screen, Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
		this.screen = screen;
		this.movie = movie;
		this.startTime = startTime;
		this.endTime = endTime;
	}
}
