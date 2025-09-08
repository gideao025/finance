package com.homelab.app.service;

import com.homelab.app.dto.request.ValeRequest;
import com.homelab.app.dto.response.SaldoValeResponse;
import com.homelab.app.dto.response.ValeResponse;
import com.homelab.app.model.Usuario;
import com.homelab.app.model.Vale;
import com.homelab.app.repository.UsuarioRepository;
import com.homelab.app.repository.ValeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ValeService {
    
    private final ValeRepository valeRepository;
    private final UsuarioRepository usuarioRepository;
    
    public ValeResponse registrarVale(ValeRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Vale vale = new Vale();
        vale.setUsuario(usuario);
        vale.setTipo(request.getTipo());
        vale.setSaldoInicial(request.getSaldoInicial());
        vale.setSaldoAtual(request.getSaldoInicial());
        vale.setValidade(request.getValidade());
        
        vale = valeRepository.save(vale);
        return ValeResponse.fromEntity(vale);
    }
    
    @Transactional(readOnly = true)
    public List<SaldoValeResponse> consultarSaldos(Long usuarioId) {
        List<Vale> vales = valeRepository.findByUsuarioIdAndSaldoAtualGreaterThan(
            usuarioId, BigDecimal.ZERO
        );
        
        return vales.stream()
            .map(SaldoValeResponse::fromEntity)
            .collect(Collectors.toList());
    }
}