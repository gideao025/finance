package com.homelab.app.service;

import com.homelab.app.dto.request.CartaoRequest;
import com.homelab.app.dto.response.CartaoResponse;
import com.homelab.app.dto.response.LimitesResponse;
import com.homelab.app.model.CartaoCredito;
import com.homelab.app.model.Usuario;
import com.homelab.app.repository.CartaoRepository;
import com.homelab.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartaoService {
    
    private final CartaoRepository cartaoRepository;
    private final UsuarioRepository usuarioRepository;
    
    public CartaoResponse cadastrarCartao(CartaoRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        CartaoCredito cartao = new CartaoCredito();
        cartao.setUsuario(usuario);
        cartao.setNome(request.getNome());
        cartao.setLimiteTotal(request.getLimiteTotal());
        cartao.setLimiteDisponivel(request.getLimiteTotal()); // Inicialmente todo o limite disponível
        cartao.setVencimento(request.getVencimento());
        
        cartao = cartaoRepository.save(cartao);
        return CartaoResponse.fromEntity(cartao);
    }
    
    @Transactional(readOnly = true)
    public List<LimitesResponse> obterLimitesCartoes(Long usuarioId) {
        List<CartaoCredito> cartoes = cartaoRepository.findByUsuarioId(usuarioId);
        
        return cartoes.stream()
            .map(LimitesResponse::fromEntity)
            .collect(Collectors.toList());
    }
}