package com.ceos22.cgv_clone.web.dto;


import lombok.Getter;

import java.time.LocalDate;

public class UserRequestDto {

    @Getter
    public static class UserSignUpDto{
        private String email;
        private String nickName;
        private String name;
        private String password;
        private String confirmPassword;
        private String phoneNumber;
        private LocalDate birth;
    }

    @Getter
    public static class UserSignInDto {
        private String email;
        private String nickName;
        private String password;
    }
}
