package com.ceos22.cgv.module.user.controller;

import com.ceos22.cgv.common.codes.SuccessCode;
import com.ceos22.cgv.module.user.dto.AuthRequest;
import com.ceos22.cgv.module.user.dto.AuthResponse;
import com.ceos22.cgv.module.user.dto.SignupRequest;
import com.ceos22.cgv.module.user.dto.SignupResponse;
import com.ceos22.cgv.module.user.service.AuthService;
import com.ceos22.cgv.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "닉네임과 비밀번호로 인증을 수행하고 액세스 토큰(JWT)을 발급합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login (
            @Valid @RequestBody final AuthRequest request) {

        ApiResponse<AuthResponse> response = ApiResponse.of(
                authService.login(request),
                SuccessCode.LOGIN_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원가입", description = "닉네임/비밀번호 등 사용자 정보를 입력받아 회원을 생성합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup (
            @Valid @RequestBody final SignupRequest request) {

        ApiResponse<SignupResponse> response = ApiResponse.of(
                authService.signup(request),
                SuccessCode.INSERT_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }
}
