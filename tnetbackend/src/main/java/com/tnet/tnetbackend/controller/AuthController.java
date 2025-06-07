package com.tnet.tnetbackend.controller;

import com.tnet.tnetbackend.dto.JwtAuthResponse;
import com.tnet.tnetbackend.dto.LoginRequest;
import com.tnet.tnetbackend.dto.RegisterDto;
import com.tnet.tnetbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    // --- این بخش آپدیت شد ---
    // حالا هم مدیر شرکت و هم مدیر کانون می‌توانند کاربر جدید بسازند
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('COMPANY_ADMIN', 'HUB_ADMIN')")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}