package com.ceos22.cgv_clone.domain.member.dto.request;


import com.ceos22.cgv_clone.domain.member.vo.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "이메일은 필수입니다")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다")
        String password
) {
        public Email toEmail() {
                return Email.from(this.email);
        }
}
