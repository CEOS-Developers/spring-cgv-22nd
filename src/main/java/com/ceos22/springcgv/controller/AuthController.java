package com.ceos22.springcgv.controller;

import com.ceos22.springcgv.dto.LoginRequestDto;
import com.ceos22.springcgv.dto.SignUpRequestDto;
import com.ceos22.springcgv.dto.TokenResponseDto;
import com.ceos22.springcgv.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto req) {
        authService.signUp(req);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/login")
//    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto req) {
//        return ResponseEntity.ok(authService.login(req));
//    }
}