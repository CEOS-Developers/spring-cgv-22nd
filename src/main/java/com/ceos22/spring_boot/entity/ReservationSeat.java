package com.ceos22.spring_boot.entity;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_seat", uniqueConstraints = @UniqueConstraint(name="uk_movie_time_seat",
	columnNames = {"movie_time_id","screen_seat_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat extends BaseEntity {
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name="movie_time_id", nullable=false)
	private MovieTime movieTime;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name="screen_seat_id", nullable=false)
	private ScreenSeat screenSeat;

	@Version
	@Column(name="version", nullable = false)
	private Long version;

	@Builder(access = PRIVATE)
	private ReservationSeat(Reservation reservation, MovieTime movieTime, ScreenSeat screenSeat) {
		this.reservation = reservation;
		this.movieTime = movieTime;
		this.screenSeat = screenSeat;
	}

	public static ReservationSeat assign(Reservation reservation, MovieTime movieTime, ScreenSeat screenSeat) {
		return ReservationSeat.builder()
			.reservation(reservation)
			.movieTime(movieTime)
			.screenSeat(screenSeat)
			.build();
	}

}
