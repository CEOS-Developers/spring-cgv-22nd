package com.ceos22.cgv_clone.domain.member.controller;

import com.ceos22.cgv_clone.domain.member.dto.LoginRequestDto;
import com.ceos22.cgv_clone.domain.member.dto.SignUpRequestDto;
import com.ceos22.cgv_clone.domain.member.service.AuthService;
import com.ceos22.cgv_clone.global.security.dto.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto requestDto) {
        memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto requestDto) {
        TokenDto tokenDto = memberService.login(requestDto.getEmail(), requestDto.getPassword());
        return ResponseEntity.ok(tokenDto);
    }
}