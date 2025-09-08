package com.homelab.app.controller;

import com.homelab.app.dto.request.TransacaoRequest;
import com.homelab.app.dto.response.FluxoCaixaResponse;
import com.homelab.app.dto.response.TransacaoResponse;
import com.homelab.app.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('USER')")
public class TransacaoController {
    
    private final TransacaoService transacaoService;
    
    @PostMapping
    public ResponseEntity<TransacaoResponse> criarTransacao(@Valid @RequestBody TransacaoRequest request) {
        TransacaoResponse response = transacaoService.criarTransacao(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Page<TransacaoResponse>> listarTransacoes(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) Long contaId,
            @RequestParam(required = false) Long cartaoId,
            @RequestParam(required = false) Long categoriaId,
            Pageable pageable) {
        
        Page<TransacaoResponse> transacoes = transacaoService.listarTransacoes(
            usuarioId, contaId, cartaoId, categoriaId, pageable
        );
        return ResponseEntity.ok(transacoes);
    }
    
    @GetMapping("/fluxo-caixa")
    public ResponseEntity<FluxoCaixaResponse> obterFluxoCaixa(@RequestParam Long usuarioId) {
        FluxoCaixaResponse response = transacaoService.calcularFluxoCaixa(usuarioId);
        return ResponseEntity.ok(response);
    }
}