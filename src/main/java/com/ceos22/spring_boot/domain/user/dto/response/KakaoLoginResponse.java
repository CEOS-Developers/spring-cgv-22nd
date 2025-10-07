package com.ceos22.spring_boot.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카카오 로그인 응답")
public class KakaoLoginResponse {

	@Schema(description = "사용자 역할", example="TMP_USER")
	private String role;

}