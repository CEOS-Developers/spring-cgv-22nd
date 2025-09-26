package com.ceos22.cgv_clone.web.dto;

import com.ceos22.cgv_clone.web.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    public static class UserSignUpResponseDto {
        private Long userId;
        private String nickName;
        private String userName;
        private String email;
        private String phoneNumber;

        public static UserSignUpResponseDto of(User user) {
            return new UserSignUpResponseDto(
                    user.getId(),
                    user.getNickName(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber()
            );
        }
    }




}
