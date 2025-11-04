package com.ceos22.springcgv.controller.auth;

import com.ceos22.springcgv.dto.user.LoginRequestDto;
import com.ceos22.springcgv.dto.user.SignUpRequestDto;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
import com.ceos22.springcgv.global.response.ApiResponse;
import com.ceos22.springcgv.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid SignUpRequestDto req) {
        authService.signUp(req);
        ApiResponse<Void> body = ApiResponse.success(201, "회원가입에 성공했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequestDto req) {
        //로그인 필터가 요청 가로챔
        throw new CustomException(ErrorCode.NOT_FOUND);
    }
}