package com.ceos22.spring_boot.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description="카카오 로그인 요청")
public class KakaoLoginRequest {

	@Schema(description = "카카오 인가 코드", example="abc123xyz")
	private String code;

}