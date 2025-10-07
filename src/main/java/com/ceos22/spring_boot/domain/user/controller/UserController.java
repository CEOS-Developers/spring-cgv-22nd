package com.ceos22.spring_boot.domain.user.controller;


import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ceos22.spring_boot.domain.common.dto.response.CommonResponse;
import com.ceos22.spring_boot.domain.user.dto.request.KakaoLoginRequest;
import com.ceos22.spring_boot.domain.user.dto.response.KakaoLoginResponse;
import com.ceos22.spring_boot.domain.user.dto.response.LogoutResponse;
import com.ceos22.spring_boot.domain.user.dto.response.ReissueResponse;
import com.ceos22.spring_boot.domain.user.exception.UserErrorCode;
import com.ceos22.spring_boot.domain.user.service.UserService;
import com.ceos22.spring_boot.entity.User;
import com.ceos22.spring_boot.global.exception.GlobalException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/api/user/login")
	@Operation(summary = "카카오 로그인", description = "카카오 인가코드를 받아 로그인합니다.")
	public CommonResponse<KakaoLoginResponse> login(@RequestBody KakaoLoginRequest request, HttpServletResponse response){
		User user = userService.handleKakaoLogin(request.getCode(), response);
		KakaoLoginResponse result = new KakaoLoginResponse(user.getRole().name());
		return CommonResponse.success(result);
	}

	@PostMapping("/api/user/reissue")
	@Operation(summary = "토큰 재발급", description = "Refresh Token을 통해 새로운 Access Token을 발급합니다.")
	public CommonResponse<ReissueResponse> reissue(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response){
		if (refreshToken == null) {
			throw new GlobalException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}
		userService.reissue(refreshToken, response);
		return CommonResponse.success(new ReissueResponse());
	}

	@PostMapping("/api/user/logout")
	@Operation(summary = "로그아웃", description = "Refresh Token을 만료시키고 로그아웃합니다.")
	public CommonResponse<LogoutResponse> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response){
		if (refreshToken == null) {
			throw new GlobalException(UserErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}
		userService.logout(refreshToken, response);
		return CommonResponse.success(new LogoutResponse());
	}



}
