package com.ceos22.cgv_clone.api.dto;

import com.ceos22.cgv_clone.domain.member.Gender;

public record SignUpRequest(
        String name,
        int age,
        Gender gender,
        String loginId,
        String password
) {
}
