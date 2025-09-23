package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

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
@Table(name = "reservation_seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id", nullable = false)
	private Reservation reservation;

	@Column(name = "row_num", nullable = false)
	private Integer rowNum;

	@Column(name = "col_num", nullable = false)
	private Integer colNum;

	@Builder(access = PRIVATE)
	private ReservationSeat(Reservation reservation, Integer rowNum, Integer colNum) {
		this.reservation = reservation;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

}
