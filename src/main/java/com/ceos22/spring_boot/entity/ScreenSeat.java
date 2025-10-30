package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screen_seat", uniqueConstraints = @UniqueConstraint(name="uk_screen_row_col", columnNames = {"screen_id", "row_num", "col_num"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScreenSeat extends BaseEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="screen_id", nullable = false)
	private Screen screen;

	@Column(name="row_num", nullable = false)
	private Integer rowNum;

	@Column(name="col_num", nullable = false)
	private Integer colNum;

	@Builder(access = PRIVATE)
	private ScreenSeat(Screen screen, Integer rowNum, Integer colNum) {
		this.screen = screen;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}
}
