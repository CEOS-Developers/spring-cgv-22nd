package com.ceos22.cgv_clone.domain.user.controller;

import com.ceos22.cgv_clone.domain.user.dto.request.LoginRequestDto;
import com.ceos22.cgv_clone.domain.user.dto.request.SignupRequestDto;
import com.ceos22.cgv_clone.domain.user.dto.response.LoginResponseDto;
import com.ceos22.cgv_clone.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public String signup(@RequestBody SignupRequestDto request) {
        log.info("[POST] /auth/signup 요청 수신 - username={}", request.getUsername());
        authService.signup(request.getUsername(), request.getPassword());
        return "회원가입 성공";
    }

    // 로그인
    @PostMapping("/auth/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        log.info("[POST] /auth/login 요청 수신 - username={}", request.getUsername());
        String token = authService.login(request.getUsername(), request.getPassword());
        return new LoginResponseDto(token, "Bearer");
    }
}
