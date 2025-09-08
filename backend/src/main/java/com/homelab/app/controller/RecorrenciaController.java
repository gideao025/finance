package com.homelab.app.controller;

import com.homelab.app.dto.request.TransacaoRecorrenteRequest;
import com.homelab.app.dto.response.TransacaoRecorrenteResponse;
import com.homelab.app.service.RecorrenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recorrencias")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('USER')")
public class RecorrenciaController {

    private final RecorrenciaService recorrenciaService;

    @PostMapping
    public ResponseEntity<TransacaoRecorrenteResponse> criarRecorrencia(
            @Valid @RequestBody TransacaoRecorrenteRequest request) {

        TransacaoRecorrenteResponse response = recorrenciaService.criarRecorrencia(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoRecorrenteResponse>> listarRecorrencias(
            @RequestParam Long usuarioId) {

        List<TransacaoRecorrenteResponse> recorrencias =
                recorrenciaService.listarRecorrenciasAtivas(usuarioId);
        return ResponseEntity.ok(recorrencias);
    }

    @PostMapping("/{id}/executar")
    public ResponseEntity<Void> executarRecorrencia(@PathVariable Long id) {
        recorrenciaService.executarRecorrenciaManual(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarRecorrencia(@PathVariable Long id) {
        recorrenciaService.desativarRecorrencia(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/identificar-padroes")
    public ResponseEntity<List<TransacaoRecorrenteResponse>> identificarPadroes(
            @RequestParam Long usuarioId) {

        List<TransacaoRecorrenteResponse> padroes =
                recorrenciaService.identificarRecorrenciasPotenciais(usuarioId);
        return ResponseEntity.ok(padroes);
    }
}