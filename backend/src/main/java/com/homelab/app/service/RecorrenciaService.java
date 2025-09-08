package com.homelab.app.service;

import com.homelab.app.model.*;
import com.homelab.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecorrenciaService {
    
    private final TransacaoRecorrenteRepository recorrenciaRepository;
    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final CartaoRepository cartaoRepository;
    
    /**
     * Processar transações recorrentes automaticamente
     * Executa diariamente às 06:00
     */
    @Scheduled(cron = "0 0 6 * * *")
    @Transactional
    public void processarRecorrenciasAutomaticas() {
        log.info("Iniciando processamento de transações recorrentes...");
        
        LocalDate hoje = LocalDate.now();
        List<TransacaoRecorrente> recorrencias = recorrenciaRepository
            .findRecorrenciasParaProcessar(hoje);
        
        log.info("Encontradas {} transações recorrentes para processar", recorrencias.size());
        
        for (TransacaoRecorrente recorrencia : recorrencias) {
            try {
                processarRecorrencia(recorrencia);
            } catch (Exception e) {
                log.error("Erro ao processar recorrência ID {}: {}", 
                    recorrencia.getId(), e.getMessage(), e);
            }
        }
        
        log.info("Processamento de recorrências concluído");
    }
    
    /**
     * Processar uma transação recorrente específica
     */
    @Transactional
    public void processarRecorrencia(TransacaoRecorrente recorrencia) {
        log.debug("Processando recorrência ID: {}", recorrencia.getId());
        
        // Validar se ainda está ativa
        if (!recorrencia.getAtivo()) {
            log.warn("Recorrência ID {} está inativa", recorrencia.getId());
            return;
        }
        
        // Verificar se não passou da data limite
        if (recorrencia.getDataFim() != null && 
            LocalDate.now().isAfter(recorrencia.getDataFim())) {
            
            desativarRecorrencia(recorrencia);
            return;
        }
        
        // Criar nova transação
        Transacao novaTransacao = criarTransacaoRecorrente(recorrencia);
        
        // Atualizar saldos
        atualizarSaldos(novaTransacao);
        
        // Atualizar próxima execução
        atualizarProximaExecucao(recorrencia);
        
        log.info("Transação recorrente processada: {} - {}", 
            recorrencia.getDescricao(), recorrencia.getValor());
    }
    
    /**
     * Criar transação a partir de recorrência
     */
    private Transacao criarTransacaoRecorrente(TransacaoRecorrente recorrencia) {
        Transacao transacao = new Transacao();
        transacao.setDescricao(recorrencia.getDescricao() + " (Recorrente)");
        transacao.setValor(recorrencia.getValor());
        transacao.setTipo(recorrencia.getTipo());
        transacao.setData(LocalDate.now());
        transacao.setConta(recorrencia.getConta());
        transacao.setCartaoCredito(recorrencia.getCartaoCredito());
        transacao.setCategoria(recorrencia.getCategoria());
        transacao.setRecorrente(true);
        transacao.setRecorrenciaId(recorrencia.getId());
        
        return transacaoRepository.save(transacao);
    }
    
    /**
     * Atualizar saldos de contas/cartões
     */
    private void atualizarSaldos(Transacao transacao) {
        if (transacao.getConta() != null) {
            atualizarSaldoConta(transacao);
        }
        
        if (transacao.getCartaoCredito() != null) {
            atualizarLimiteCartao(transacao);
        }
    }
    
    private void atualizarSaldoConta(Transacao transacao) {
        Conta conta = transacao.getConta();
        BigDecimal novoSaldo;
        
        if (transacao.getTipo() == TipoTransacao.RECEITA) {
            novoSaldo = conta.getSaldo().add(transacao.getValor());
        } else {
            novoSaldo = conta.getSaldo().subtract(transacao.getValor());
        }
        
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
        
        log.debug("Saldo da conta {} atualizado para: {}", 
            conta.getId(), novoSaldo);
    }
    
    private void atualizarLimiteCartao(Transacao transacao) {
        CartaoCredito cartao = transacao.getCartaoCredito();
        BigDecimal novoLimiteUtilizado = cartao.getLimiteUtilizado()
            .add(transacao.getValor());
        
        cartao.setLimiteUtilizado(novoLimiteUtilizado);
        cartaoRepository.save(cartao);
        
        log.debug("Limite utilizado do cartão {} atualizado para: {}", 
            cartao.getId(), novoLimiteUtilizado);
    }
    
    /**
     * Atualizar próxima data de execução
     */
    private void atualizarProximaExecucao(TransacaoRecorrente recorrencia) {
        LocalDate proximaData = recorrencia.calcularProximaExecucao();
        recorrencia.setProximaExecucao(proximaData);
        recorrenciaRepository.save(recorrencia);
        
        log.debug("Próxima execução da recorrência ID {} agendada para: {}", 
            recorrencia.getId(), proximaData);
    }
    
    /**
     * Desativar recorrência expirada
     */
    private void desativarRecorrencia(TransacaoRecorrente recorrencia) {
        recorrencia.setAtivo(false);
        recorrenciaRepository.save(recorrencia);
        
        log.info("Recorrência ID {} desativada - data fim atingida", 
            recorrencia.getId());
    }
    
    // === MÉTODOS PÚBLICOS ===
    
    /**
     * Identificar transações potencialmente recorrentes
     */
    public List<TransacaoRecorrente> identificarRecorrenciasPotenciais(Long usuarioId) {
        log.info("Analisando transações do usuário {} para identificar padrões recorrentes", usuarioId);
        
        // Buscar transações similares dos últimos 90 dias
        List<Transacao> transacoesRecentes = transacaoRepository
            .findTransacoesByUsuarioUltimos90Dias(usuarioId);
        
        // Agrupar por descrição e valor similar
        return analisarPadroesRecorrentes(transacoesRecentes);
    }
    
    /**
     * Criar nova recorrência
     */
    @Transactional
    public TransacaoRecorrente criarRecorrencia(TransacaoRecorrente recorrencia) {
        log.info("Criando nova recorrência: {}", recorrencia.getDescricao());
        
        // Validar dados
        validarRecorrencia(recorrencia);
        
        // Definir próxima execução
        if (recorrencia.getProximaExecucao() == null) {
            recorrencia.setProximaExecucao(recorrencia.getDataInicio());
        }
        
        return recorrenciaRepository.save(recorrencia);
    }
    
    /**
     * Executar recorrência manualmente
     */
    @Transactional
    public void executarRecorrenciaManual(Long recorrenciaId) {
        TransacaoRecorrente recorrencia = recorrenciaRepository
            .findById(recorrenciaId)
            .orElseThrow(() -> new RuntimeException("Recorrência não encontrada"));
        
        processarRecorrencia(recorrencia);
    }
    
    /**
     * Listar recorrências ativas do usuário
     */
    public List<TransacaoRecorrente> listarRecorrenciasAtivas(Long usuarioId) {
        return recorrenciaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }
    
    /**
     * Desativar recorrência
     */
    @Transactional
    public void desativarRecorrencia(Long recorrenciaId) {
        TransacaoRecorrente recorrencia = recorrenciaRepository
            .findById(recorrenciaId)
            .orElseThrow(() -> new RuntimeException("Recorrência não encontrada"));
        
        recorrencia.setAtivo(false);
        recorrenciaRepository.save(recorrencia);
        
        log.info("Recorrência ID {} desativada pelo usuário", recorrenciaId);
    }
    
    // === MÉTODOS PRIVADOS ===
    
    private List<TransacaoRecorrente> analisarPadroesRecorrentes(List<Transacao> transacoes) {
        // Implementação simplificada - poderia ser mais sofisticada
        // Agrupa transações por descrição similar e analisa frequência
        
        // TODO: Implementar algoritmo de análise de padrões
        // Por exemplo: detectar transações com mesmo valor e descrição 
        // que ocorrem em intervalos regulares
        
        return List.of(); // Retorna lista vazia por enquanto
    }
    
    private void validarRecorrencia(TransacaoRecorrente recorrencia) {
        // Validar se conta ou cartão pertencem ao usuário
        if (recorrencia.getConta() != null && 
            !recorrencia.getConta().getUsuario().getId().equals(recorrencia.getUsuario().getId())) {
            throw new RuntimeException("Conta não pertence ao usuário");
        }
        
        if (recorrencia.getCartaoCredito() != null && 
            !recorrencia.getCartaoCredito().getUsuario().getId().equals(recorrencia.getUsuario().getId())) {
            throw new RuntimeException("Cartão não pertence ao usuário");
        }
        
        // Validar se tem conta OU cartão
        if (recorrencia.getConta() == null && recorrencia.getCartaoCredito() == null) {
            throw new RuntimeException("Recorrência deve ter conta ou cartão associado");
        }
        
        // Validar datas
        if (recorrencia.getDataFim() != null && 
            recorrencia.getDataFim().isBefore(recorrencia.getDataInicio())) {
            throw new RuntimeException("Data fim deve ser posterior à data início");
        }
    }
}