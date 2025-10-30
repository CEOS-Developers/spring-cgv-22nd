package com.ceos22.cgv_clone.domain.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BirthDate {

    @Column(name = "birth_date", nullable = false)
    private LocalDate value;

    private BirthDate(LocalDate value) {
        validate(value);
        this.value = value;
    }

    public static BirthDate from(LocalDate value) {
        return new BirthDate(value);
    }

    public void validate(LocalDate value) {
        if(value == null) {
            throw new IllegalArgumentException("생년월일은 필수입니다");
        }
        // 가입 연령 조건 추가
    }

    public int getAge() {
        return Period.between(value, LocalDate.now()).getYears();
    }

    public boolean isAdult() {
        return getAge() >= 19;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof BirthDate other &&
                Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
