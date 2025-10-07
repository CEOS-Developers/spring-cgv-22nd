package com.ceos22.cgv_clone.domain.dto;

import com.ceos22.cgv_clone.domain.member.Gender;

public record SignUpReq(
        String name,
        int age,
        Gender gender,
        String loginId,
        String password
) {
}
