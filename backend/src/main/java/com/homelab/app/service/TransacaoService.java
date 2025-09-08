package com.homelab.app.service;

import com.homelab.app.dto.request.TransacaoRequest;
import com.homelab.app.dto.response.FluxoCaixaResponse;
import com.homelab.app.dto.response.TransacaoResponse;
import com.homelab.app.model.*;
import com.homelab.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransacaoService {
    
    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final CartaoRepository cartaoRepository;
    private final CategoriaRepository categoriaRepository;
    
    public TransacaoResponse criarTransacao(TransacaoRequest request) {
        Transacao transacao = new Transacao();
        transacao.setValor(request.getValor());
        transacao.setTipo(request.getTipo());
        transacao.setData(request.getData() != null ? request.getData() : LocalDateTime.now());
        transacao.setRecorrencia(request.getRecorrencia());
        transacao.setDescricao(request.getDescricao());
        
        // Buscar categoria
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        transacao.setCategoria(categoria);
        
        // Definir origem (conta ou cartão)
        if (request.getContaId() != null) {
            Conta conta = contaRepository.findById(request.getContaId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
            transacao.setConta(conta);
            atualizarSaldoConta(conta, transacao);
        } else if (request.getCartaoId() != null) {
            CartaoCredito cartao = cartaoRepository.findById(request.getCartaoId())
                .orElseThrow(() -> new RuntimeException("Cartão não encontrado"));
            transacao.setCartaoCredito(cartao);
            atualizarLimiteCartao(cartao, transacao);
        } else {
            throw new RuntimeException("Deve informar conta ou cartão");
        }
        
        transacao = transacaoRepository.save(transacao);
        return TransacaoResponse.fromEntity(transacao);
    }
    
    @Transactional(readOnly = true)
    public Page<TransacaoResponse> listarTransacoes(Long usuarioId, Long contaId, 
            Long cartaoId, Long categoriaId, Pageable pageable) {
        
        Page<Transacao> transacoes = transacaoRepository.findByFilters(
            usuarioId, contaId, cartaoId, categoriaId, pageable
        );
        
        return transacoes.map(TransacaoResponse::fromEntity);
    }
    
    @Transactional(readOnly = true)
    public FluxoCaixaResponse calcularFluxoCaixa(Long usuarioId) {
        BigDecimal totalEntradas = transacaoRepository.sumReceitasByUsuarioId(usuarioId);
        BigDecimal totalSaidas = transacaoRepository.sumDespesasByUsuarioId(usuarioId);
        BigDecimal saldoProjetado = totalEntradas.subtract(totalSaidas);
        
        return FluxoCaixaResponse.builder()
            .totalEntradas(totalEntradas)
            .totalSaidas(totalSaidas)
            .saldoProjetado(saldoProjetado)
            .build();
    }
    
    private void atualizarSaldoConta(Conta conta, Transacao transacao) {
        BigDecimal novoSaldo = conta.getSaldoAtual();
        
        if (transacao.getTipo() == TipoTransacao.RECEITA) {
            novoSaldo = novoSaldo.add(transacao.getValor());
        } else if (transacao.getTipo() == TipoTransacao.DESPESA) {
            novoSaldo = novoSaldo.subtract(transacao.getValor());
        }
        
        conta.setSaldoAtual(novoSaldo);
        contaRepository.save(conta);
    }
    
    private void atualizarLimiteCartao(CartaoCredito cartao, Transacao transacao) {
        if (transacao.getTipo() == TipoTransacao.DESPESA) {
            BigDecimal novoLimite = cartao.getLimiteDisponivel().subtract(transacao.getValor());
            
            if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Limite insuficiente no cartão");
            }
            
            cartao.setLimiteDisponivel(novoLimite);
            cartaoRepository.save(cartao);
        }
    }
}