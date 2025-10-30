package com.ceos22.cgv_clone.domain.user.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}
