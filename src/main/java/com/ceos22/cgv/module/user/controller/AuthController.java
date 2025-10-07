package com.ceos22.cgv.module.user.controller;

import com.ceos22.cgv.codes.SuccessCode;
import com.ceos22.cgv.module.cinema.dto.CinemaListResponse;
import com.ceos22.cgv.module.user.dto.AuthRequest;
import com.ceos22.cgv.module.user.dto.AuthResponse;
import com.ceos22.cgv.module.user.dto.SignupRequest;
import com.ceos22.cgv.module.user.dto.SignupResponse;
import com.ceos22.cgv.module.user.service.AuthService;
import com.ceos22.cgv.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("api/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login (
            @Valid @RequestBody final AuthRequest request) {

        ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder()
                .response(authService.login(request))
                .statusCode(SuccessCode.LOGIN_SUCCESS.getStatusCode())
                .message(SuccessCode.LOGIN_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("api/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup (
            @Valid @RequestBody final SignupRequest request) {

        ApiResponse<SignupResponse> response = ApiResponse.<SignupResponse>builder()
                .response(authService.signup(request))
                .statusCode(SuccessCode.INSERT_SUCCESS.getStatusCode())
                .message(SuccessCode.INSERT_SUCCESS.getMessage())
                .build();

        return ResponseEntity.ok().body(response);
    }
}
