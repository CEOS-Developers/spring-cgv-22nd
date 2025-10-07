package com.ceos22.cgv_clone.domain.auth.controller;

import com.ceos22.cgv_clone.common.error.SuccessCode;
import com.ceos22.cgv_clone.common.response.ApiResponse;
import com.ceos22.cgv_clone.domain.auth.service.AuthService;
import com.ceos22.cgv_clone.domain.member.dto.request.LoginRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.RefreshTokenRequest;
import com.ceos22.cgv_clone.domain.member.dto.request.SignUpRequest;
import com.ceos22.cgv_clone.domain.member.dto.response.MemberInfoResponse;
import com.ceos22.cgv_clone.domain.member.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> signup(
            @Valid @RequestBody SignUpRequest request
    ) {
        MemberInfoResponse response = authService.signUp(request);
        return ApiResponse.success(SuccessCode.SIGNUP_SUCCESS, response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        TokenResponse response = authService.login(request);
        return ApiResponse.success(SuccessCode.LOGIN_SUCCESS, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        TokenResponse response = authService.refresh(request);
        return ApiResponse.success(SuccessCode.REFRESH_SUCCESS, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        return ApiResponse.success(SuccessCode.LOGOUT_SUCCESS);
    }
}
