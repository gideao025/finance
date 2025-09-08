package com.homelab.app.controller;

import com.homelab.app.dto.request.CartaoRequest;
import com.homelab.app.dto.response.CartaoResponse;
import com.homelab.app.dto.response.LimitesResponse;
import com.homelab.app.service.CartaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('USER')")
public class CartaoController {
    
    private final CartaoService cartaoService;
    
    @PostMapping
    public ResponseEntity<CartaoResponse> cadastrarCartao(@Valid @RequestBody CartaoRequest request) {
        CartaoResponse response = cartaoService.cadastrarCartao(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/limites")
    public ResponseEntity<List<LimitesResponse>> obterLimites(@RequestParam Long usuarioId) {
        List<LimitesResponse> limites = cartaoService.obterLimitesCartoes(usuarioId);
        return ResponseEntity.ok(limites);
    }
}