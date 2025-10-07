package com.ceos22.cgv_clone.api;

import com.ceos22.cgv_clone.domain.dto.LoginReq;
import com.ceos22.cgv_clone.domain.dto.LoginRes;
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
    public LoginRes login(@RequestBody LoginReq req) {
        return loginService.login(req);
    }
}
