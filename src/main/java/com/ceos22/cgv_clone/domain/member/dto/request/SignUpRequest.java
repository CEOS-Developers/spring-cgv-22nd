package com.ceos22.cgv_clone.domain.member.dto.request;

import com.ceos22.cgv_clone.domain.member.vo.BirthDate;
import com.ceos22.cgv_clone.domain.member.vo.Email;
import com.ceos22.cgv_clone.domain.member.vo.MemberName;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotNull(message = "생년월일은 필수입니다.")
        LocalDate birthDate
) {
        public Email toEmail() {
                return Email.from(this.email);
        }

        public MemberName toName() {
                return MemberName.from(this.name);
        }

        public BirthDate toBirthDate() {
                return BirthDate.from(this.birthDate);
        }
}
