package com.spring.memory.controller;

import com.spring.memory.dto.user.*;
import com.spring.memory.entity.CustomUserDetails;
import com.spring.memory.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO req) {
        UserDTO dto = authService.register(req);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO req) {
        LoginResponseDTO dto = authService.login(req);
        return ResponseEntity.status(200).body(dto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO req) {
        LoginResponseDTO dto = authService.refreshToken(req.getRefreshToken());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        LogoutResponseDTO dto = authService.logout(user.getId());
        return ResponseEntity.ok(dto);
    }

}
