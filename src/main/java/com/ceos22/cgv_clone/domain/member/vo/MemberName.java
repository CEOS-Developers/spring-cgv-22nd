package com.ceos22.cgv_clone.domain.member.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberName {

    @Column(name = "name", nullable = false)
    private String value;

    private MemberName(String value) {
        validate(value);
        this.value = value;
    }

    public static MemberName from(String value) {
        return new MemberName(value);
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof MemberName other &&
                Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
