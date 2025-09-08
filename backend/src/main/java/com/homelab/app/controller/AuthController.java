package com.homelab.app.controller;

import com.homelab.app.dto.request.LoginRequest;
import com.homelab.app.dto.request.UsuarioRequest;
import com.homelab.app.dto.response.AuthResponse;
import com.homelab.app.dto.response.UsuarioResponse;
import com.homelab.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = authService.cadastrarUsuario(request);
        return ResponseEntity.ok(response);
    }
}