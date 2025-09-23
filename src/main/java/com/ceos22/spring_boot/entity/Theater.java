package com.ceos22.spring_boot.entity;

import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "theater")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Builder(access = PRIVATE)
	public Theater(String name, String address) {
		this.name = name;
		this.address = address;
	}
}
