package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.LoginRequest;
import com.ceos22.cgv_clone.domain.dto.LoginResponse;
import com.ceos22.cgv_clone.domain.dto.SignUpReq;
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
    public void signUp(@RequestBody SignUpReq req) {
        authService.signUp(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        return loginService.login(req);
    }
}
