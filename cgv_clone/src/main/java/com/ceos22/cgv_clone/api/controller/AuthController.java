package com.ceos22.cgv_clone.api.controller;

import com.ceos22.cgv_clone.api.dto.LoginRequest;
import com.ceos22.cgv_clone.api.dto.LoginResponse;
import com.ceos22.cgv_clone.api.dto.SignUpRequest;
import com.ceos22.cgv_clone.service.AuthService;
import com.ceos22.cgv_clone.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final LoginService loginService;

    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpRequest req) {
        authService.signUp(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return loginService.login(req);
    }
}
