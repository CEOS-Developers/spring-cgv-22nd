package com.ceos22.cgv_clone.domain.member.controller;

import com.ceos22.cgv_clone.domain.member.dto.LoginRequestDto;
import com.ceos22.cgv_clone.domain.member.dto.SignUpRequestDto;
import com.ceos22.cgv_clone.domain.member.service.AuthService;
import com.ceos22.cgv_clone.global.apiPayload.CustomResponse;
import com.ceos22.cgv_clone.global.apiPayload.code.success.SuccessCode;
import com.ceos22.cgv_clone.global.security.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="회원 관련 API", description = "회원가입/로그인")
public class AuthController {

    private final AuthService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public CustomResponse<?> signup(@RequestBody SignUpRequestDto request) {
        memberService.signup(request);
        return CustomResponse.onSuccess(SuccessCode.MEMBER_CREATED);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public CustomResponse<TokenDto> login(@RequestBody LoginRequestDto request) {
        TokenDto tokenDto = memberService.login(request.getEmail(), request.getPassword());
        return CustomResponse.onSuccess(SuccessCode.LOGIN_SUCCESS, tokenDto);
    }
}