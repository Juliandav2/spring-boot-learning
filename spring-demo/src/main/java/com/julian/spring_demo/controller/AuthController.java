package com.julian.spring_demo.controller;

import com.julian.spring_demo.dto.AuthResponseDTO;
import com.julian.spring_demo.dto.LoginRequestDTO;
import com.julian.spring_demo.dto.RefreshRequestDTO;
import com.julian.spring_demo.dto.RegisterRequestDTO;
import com.julian.spring_demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/auth")
@Tag (name = "Authentication", description = "Auth management API")
public class AuthController {

    private final AuthService authService;

    public AuthController (AuthService authService) {
        this.authService = authService;
    }

    @Operation (summary = "Register a new user")
    @PostMapping ("/register")
    public ResponseEntity<AuthResponseDTO> register (@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @Operation (summary = "Login and get JWT token")
    @PostMapping ("/login")
    public ResponseEntity<AuthResponseDTO> login (@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation (summary = "Refresh access token")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh (@Valid @RequestBody RefreshRequestDTO dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    @Operation (summary = "Logout and revoke refresh token")
    @PostMapping ("/logout")
    public ResponseEntity<Void> logout (@Valid @RequestBody RefreshRequestDTO dto) {
        authService.logout(dto);
        return ResponseEntity.noContent().build();
    }
}
