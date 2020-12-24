package org.izomp.transaction.manager.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.izomp.transaction.manager.auth.model.LoginRequestDto;
import org.izomp.transaction.manager.auth.model.SignUpRequestDto;
import org.izomp.transaction.manager.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(AuthController.AUTH_PATH)
@RequiredArgsConstructor
public class AuthController {
    public static final String AUTH_PATH = "/auth";

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<String> acceptRegister(@RequestBody SignUpRequestDto signUpRequest) {
        return ResponseEntity.ok(authService.register(signUpRequest));
    }

}
