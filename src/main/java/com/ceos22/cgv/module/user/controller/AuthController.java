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

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login (
            @Valid @RequestBody final AuthRequest request) {

        ApiResponse<AuthResponse> response = ApiResponse.of(
                authService.login(request),
                SuccessCode.LOGIN_SUCCESS
        );

        return ResponseEntity.ok().body(response);
    }

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
