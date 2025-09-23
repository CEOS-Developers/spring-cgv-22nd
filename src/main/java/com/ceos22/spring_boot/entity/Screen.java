package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import com.ceos22.spring_boot.entity.enums.ScreenType;

import jakarta.persistence.Column;
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
@Table(name = "screen")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseEntity{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theater_id", nullable = false)
	private Theater theater;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ScreenType type;

	@Column(name = "row_count", nullable = false)
	private Integer rowCount;

	@Column(name = "col_count", nullable = false)
	private Integer colCount;

	@Builder(access = PRIVATE)
	private Screen(Theater theater, ScreenType type, Integer rowCount, Integer colCount) {
		this.theater = theater;
		this.type = type;
		this.rowCount = rowCount;
		this.colCount = colCount;
	}

}
