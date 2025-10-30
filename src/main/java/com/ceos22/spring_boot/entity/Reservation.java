package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import com.ceos22.spring_boot.entity.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_time_id", nullable = false)
	private MovieTime movieTime;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@Builder(access = PRIVATE)
	private Reservation(User user, MovieTime movieTime, ReservationStatus status){
		this.user = user;
		this.movieTime = movieTime;
		this.status = status;
	}

	public static Reservation create(User user, MovieTime movieTime, ReservationStatus status) {
		return Reservation.builder()
			.user(user)
			.movieTime(movieTime)
			.status(status)
			.build();
	}

	public void setStatus(ReservationStatus status){ this.status = status; }

}
