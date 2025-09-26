package com.ceos22.cgv_clone.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    public static class UserSignUpDto{
        private String email;
        private String nickName;
        private String name;
        private String password;
        private String confirmPassword;
        private String phoneNumber;
    }
}
