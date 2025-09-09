package com.homelab.app.service;

import com.homelab.app.model.*;
import com.homelab.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RecorrenciaServiceIntegrationTest {

    @Autowired
    private RecorrenciaService recorrenciaService;

    @Autowired
    private TransacaoRecorrenteRepository recorrenciaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Usuario usuario;
    private Conta conta;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        // Criar dados de teste
        usuario = new Usuario();
        usuario.setNome("João Teste");
        usuario.setEmail("joao.teste@email.com");
        usuario.setSenha("senha123");
        usuario = usuarioRepository.save(usuario);

        conta = new Conta();
        conta.setNome("Conta Teste");
        conta.setSaldo(new BigDecimal("2000.00"));
        conta.setUsuario(usuario);
        conta = contaRepository.save(conta);

        categoria = new Categoria();
        categoria.setNome("Categoria Teste");
        categoria = categoriaRepository.save(categoria);
    }

    @Test
    void integracaoCompleta_CriarEProcessarRecorrenciaMensal() {
        // Arrange - Criar recorrência mensal
        TransacaoRecorrente recorrencia = new TransacaoRecorrente();
        recorrencia.setDescricao("Salário Teste");
        recorrencia.setValor(new BigDecimal("4000.00"));
        recorrencia.setTipo(TipoTransacao.RECEITA);
        recorrencia.setPeriodicidade(Periodicidade.MENSAL);
        recorrencia.setDataInicio(LocalDate.now());
        recorrencia.setProximaExecucao(LocalDate.now());
        recorrencia.setAtivo(true);
        recorrencia.setConta(conta);
        recorrencia.setCategoria(categoria);
        recorrencia.setUsuario(usuario);

        // Act - Criar recorrência
        TransacaoRecorrente recorrenciaCriada = recorrenciaService.criarRecorrencia(recorrencia);

        // Assert - Verificar criação
        assertNotNull(recorrenciaCriada.getId());
        assertTrue(recorrenciaCriada.getAtivo());

        // Verificar saldo antes do processamento
        BigDecimal saldoAntes = conta.getSaldo();

        // Act - Processar recorrência
        recorrenciaService.processarRecorrencia(recorrenciaCriada);

        // Assert - Verificar processamento
        List<Transacao> transacoesCriadas = transacaoRepository
                .findAll()
                .stream()
                .filter(t -> t.getRecorrente() != null && t.getRecorrente())
                .toList();

        assertEquals(1, transacoesCriadas.size());

        Transacao transacaoCriada = transacoesCriadas.get(0);
        assertEquals("Salário Teste (Recorrente)", transacaoCriada.getDescricao());
        assertEquals(new BigDecimal("4000.00"), transacaoCriada.getValor());
        assertEquals(TipoTransacao.RECEITA, transacaoCriada.getTipo());
        assertTrue(transacaoCriada.getRecorrente());

        // Verificar atualização do saldo
        conta = contaRepository.findById(conta.getId()).orElseThrow();
        BigDecimal saldoEsperado = saldoAntes.add(new BigDecimal("4000.00"));
        assertEquals(0, saldoEsperado.compareTo(conta.getSaldo()));

        // Verificar próxima execução
        recorrenciaCriada = recorrenciaRepository.findById(recorrenciaCriada.getId()).orElseThrow();
        assertEquals(LocalDate.now().plusMonths(1), recorrenciaCriada.getProximaExecucao());
    }

    @Test
    void processarRecorrenciasAutomaticas_MultiplasRecorrencias() {
        // Arrange - Criar múltiplas recorrências
        TransacaoRecorrente receita = criarRecorrenciaReceita();
        TransacaoRecorrente despesa = criarRecorrenciaDespesa();

        recorrenciaRepository.save(receita);
        recorrenciaRepository.save(despesa);

        BigDecimal saldoInicial = conta.getSaldo();

        // Act - Processar automaticamente
        recorrenciaService.processarRecorrenciasAutomaticas();

        // Assert - Verificar processamento
        List<Transacao> todasTransacoes = transacaoRepository.findAll();
        long transacoesRecorrentes = todasTransacoes.stream()
                .filter(t -> t.getRecorrente() != null && t.getRecorrente())
                .count();

        assertEquals(2, transacoesRecorrentes);

        // Verificar saldo final
        conta = contaRepository.findById(conta.getId()).orElseThrow();
        BigDecimal saldoEsperado = saldoInicial
                .add(receita.getValor())  // +3000
                .subtract(despesa.getValor()); // -800

        assertEquals(0, saldoEsperado.compareTo(conta.getSaldo()));
    }

    // === MÉTODOS AUXILIARES ===

    private TransacaoRecorrente criarRecorrenciaReceita() {
        TransacaoRecorrente recorrencia = new TransacaoRecorrente();
        recorrencia.setDescricao("Freelance");
        recorrencia.setValor(new BigDecimal("3000.00"));
        recorrencia.setTipo(TipoTransacao.RECEITA);
        recorrencia.setPeriodicidade(Periodicidade.MENSAL);
        recorrencia.setDataInicio(LocalDate.now());
        recorrencia.setProximaExecucao(LocalDate.now());
        recorrencia.setAtivo(true);
        recorrencia.setConta(conta);
        recorrencia.setCategoria(categoria);
        recorrencia.setUsuario(usuario);
        return recorrencia;
    }

    private TransacaoRecorrente criarRecorrenciaDespesa() {
        TransacaoRecorrente recorrencia = new TransacaoRecorrente();
        recorrencia.setDescricao("Academia");
        recorrencia.setValor(new BigDecimal("800.00"));
        recorrencia.setTipo(TipoTransacao.DESPESA);
        recorrencia.setPeriodicidade(Periodicidade.MENSAL);
        recorrencia.setDataInicio(LocalDate.now());
        recorrencia.setProximaExecucao(LocalDate.now());
        recorrencia.setAtivo(true);
        recorrencia.setConta(conta);
        recorrencia.setCategoria(categoria);
        recorrencia.setUsuario(usuario);
        return recorrencia;
    }
}