package com.ceos22.springcgv.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
