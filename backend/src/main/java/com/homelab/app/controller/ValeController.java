package com.homelab.app.controller;

import com.homelab.app.dto.request.ValeRequest;
import com.homelab.app.dto.response.ValeResponse;
import com.homelab.app.dto.response.SaldoValeResponse;
import com.homelab.app.service.ValeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vales")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('USER')")
public class ValeController {
    
    private final ValeService valeService;
    
    @PostMapping
    public ResponseEntity<ValeResponse> registrarVale(@Valid @RequestBody ValeRequest request) {
        ValeResponse response = valeService.registrarVale(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/saldo")
    public ResponseEntity<List<SaldoValeResponse>> consultarSaldos(@RequestParam Long usuarioId) {
        List<SaldoValeResponse> saldos = valeService.consultarSaldos(usuarioId);
        return ResponseEntity.ok(saldos);
    }
}