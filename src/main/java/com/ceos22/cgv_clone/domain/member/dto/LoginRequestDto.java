package com.ceos22.cgv_clone.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}