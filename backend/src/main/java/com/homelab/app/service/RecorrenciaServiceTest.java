package com.homelab.app.service;

import com.homelab.app.model.*;
import com.homelab.app.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecorrenciaServiceTest {

    @Mock
    private TransacaoRecorrenteRepository recorrenciaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private RecorrenciaService recorrenciaService;

    private Usuario usuarioTeste;
    private Conta contaTeste;
    private CartaoCredito cartaoTeste;
    private Categoria categoriaTeste;
    private TransacaoRecorrente recorrenciaTeste;

    @BeforeEach
    void setUp() {
        // Setup usuário
        usuarioTeste = new Usuario();
        usuarioTeste.setId(1L);
        usuarioTeste.setNome("João Silva");
        usuarioTeste.setEmail("joao@email.com");

        // Setup conta
        contaTeste = new Conta();
        contaTeste.setId(1L);
        contaTeste.setNome("Conta Corrente");
        contaTeste.setSaldo(new BigDecimal("1000.00"));
        contaTeste.setUsuario(usuarioTeste);

        // Setup cartão
        cartaoTeste = new CartaoCredito();
        cartaoTeste.setId(1L);
        cartaoTeste.setNome("Cartão Master");
        cartaoTeste.setLimite(new BigDecimal("5000.00"));
        cartaoTeste.setLimiteUtilizado(new BigDecimal("1500.00"));
        cartaoTeste.setUsuario(usuarioTeste);

        // Setup categoria
        categoriaTeste = new Categoria();
        categoriaTeste.setId(1L);
        categoriaTeste.setNome("Alimentação");

        // Setup recorrência
        recorrenciaTeste = new TransacaoRecorrente();
        recorrenciaTeste.setId(1L);
        recorrenciaTeste.setDescricao("Salário");
        recorrenciaTeste.setValor(new BigDecimal("5000.00"));
        recorrenciaTeste.setTipo(TipoTransacao.RECEITA);
        recorrenciaTeste.setPeriodicidade(Periodicidade.MENSAL);
        recorrenciaTeste.setDataInicio(LocalDate.now().minusMonths(1));
        recorrenciaTeste.setProximaExecucao(LocalDate.now());
        recorrenciaTeste.setAtivo(true);
        recorrenciaTeste.setConta(contaTeste);
        recorrenciaTeste.setCategoria(categoriaTeste);
        recorrenciaTeste.setUsuario(usuarioTeste);
    }

    @Test
    void processarRecorrenciasAutomaticas_DeveProcessarTodasRecorrenciasPendentes() {
        // Arrange
        LocalDate hoje = LocalDate.now();
        List<TransacaoRecorrente> recorrenciasPendentes = Arrays.asList(
                recorrenciaTeste,
                criarRecorrenciaDespesa()
        );

        when(recorrenciaRepository.findRecorrenciasParaProcessar(hoje))
                .thenReturn(recorrenciasPendentes);
        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrenciasAutomaticas();

        // Assert
        verify(recorrenciaRepository, times(1)).findRecorrenciasParaProcessar(hoje);
        verify(transacaoRepository, times(2)).save(any(Transacao.class));
        verify(contaRepository, times(2)).save(any(Conta.class));
        verify(recorrenciaRepository, times(2)).save(any(TransacaoRecorrente.class));
    }

    @Test
    void processarRecorrencia_ReceitaConta_DeveAtualizarSaldoCorretamente() {
        // Arrange
        BigDecimal saldoOriginal = contaTeste.getSaldo();
        BigDecimal valorRecorrencia = recorrenciaTeste.getValor();

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(transacaoRepository).save(argThat(transacao ->
                transacao.getDescricao().contains("Recorrente") &&
                        transacao.getValor().equals(valorRecorrencia) &&
                        transacao.getTipo() == TipoTransacao.RECEITA &&
                        transacao.getRecorrente() == true
        ));

        verify(contaRepository).save(argThat(conta ->
                conta.getSaldo().equals(saldoOriginal.add(valorRecorrencia))
        ));
    }

    @Test
    void processarRecorrencia_DespesaConta_DeveSubtrairDoSaldo() {
        // Arrange
        recorrenciaTeste.setTipo(TipoTransacao.DESPESA);
        recorrenciaTeste.setValor(new BigDecimal("200.00"));

        BigDecimal saldoOriginal = contaTeste.getSaldo();
        BigDecimal valorDespesa = recorrenciaTeste.getValor();

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(contaRepository).save(argThat(conta ->
                conta.getSaldo().equals(saldoOriginal.subtract(valorDespesa))
        ));
    }

    @Test
    void processarRecorrencia_DespesaCartao_DeveAtualizarLimiteUtilizado() {
        // Arrange
        recorrenciaTeste.setConta(null);
        recorrenciaTeste.setCartaoCredito(cartaoTeste);
        recorrenciaTeste.setTipo(TipoTransacao.DESPESA);
        recorrenciaTeste.setValor(new BigDecimal("300.00"));

        BigDecimal limiteOriginal = cartaoTeste.getLimiteUtilizado();
        BigDecimal valorDespesa = recorrenciaTeste.getValor();

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(cartaoRepository.save(any(CartaoCredito.class)))
                .thenReturn(cartaoTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(cartaoRepository).save(argThat(cartao ->
                cartao.getLimiteUtilizado().equals(limiteOriginal.add(valorDespesa))
        ));
    }

    @Test
    void processarRecorrencia_PeriodicidadeMensal_DeveCalcularProximaExecucaoCorreta() {
        // Arrange
        LocalDate dataAtual = LocalDate.of(2024, 1, 15);
        recorrenciaTeste.setPeriodicidade(Periodicidade.MENSAL);
        recorrenciaTeste.setProximaExecucao(dataAtual);

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(recorrenciaRepository).save(argThat(recorrencia ->
                recorrencia.getProximaExecucao().equals(dataAtual.plusMonths(1))
        ));
    }

    @Test
    void processarRecorrencia_PeriodicidadeSemanal_DeveCalcularProximaExecucaoCorreta() {
        // Arrange
        LocalDate dataAtual = LocalDate.of(2024, 1, 15);
        recorrenciaTeste.setPeriodicidade(Periodicidade.SEMANAL);
        recorrenciaTeste.setProximaExecucao(dataAtual);

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(recorrenciaRepository).save(argThat(recorrencia ->
                recorrencia.getProximaExecucao().equals(dataAtual.plusWeeks(1))
        ));
    }

    @Test
    void processarRecorrencia_RecorrenciaInativa_NaoDeveProcessar() {
        // Arrange
        recorrenciaTeste.setAtivo(false);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(transacaoRepository, never()).save(any(Transacao.class));
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void processarRecorrencia_DataFimVencida_DeveDesativarRecorrencia() {
        // Arrange
        recorrenciaTeste.setDataFim(LocalDate.now().minusDays(1));

        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(recorrenciaRepository).save(argThat(recorrencia ->
                !recorrencia.getAtivo()
        ));
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void criarRecorrencia_DadosValidos_DeveCriarComSucesso() {
        // Arrange
        TransacaoRecorrente novaRecorrencia = new TransacaoRecorrente();
        novaRecorrencia.setDescricao("Internet");
        novaRecorrencia.setValor(new BigDecimal("89.90"));
        novaRecorrencia.setTipo(TipoTransacao.DESPESA);
        novaRecorrencia.setPeriodicidade(Periodicidade.MENSAL);
        novaRecorrencia.setDataInicio(LocalDate.now());
        novaRecorrencia.setConta(contaTeste);
        novaRecorrencia.setCategoria(categoriaTeste);
        novaRecorrencia.setUsuario(usuarioTeste);

        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(novaRecorrencia);

        // Act
        TransacaoRecorrente resultado = recorrenciaService.criarRecorrencia(novaRecorrencia);

        // Assert
        assertNotNull(resultado);
        verify(recorrenciaRepository).save(novaRecorrencia);
        assertEquals(novaRecorrencia.getDataInicio(), novaRecorrencia.getProximaExecucao());
    }

    @Test
    void criarRecorrencia_ContaNaoPerteceAoUsuario_DeveLancarExcecao() {
        // Arrange
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        contaTeste.setUsuario(outroUsuario);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recorrenciaService.criarRecorrencia(recorrenciaTeste);
        });

        assertEquals("Conta não pertence ao usuário", exception.getMessage());
        verify(recorrenciaRepository, never()).save(any());
    }

    @Test
    void criarRecorrencia_SemContaECartao_DeveLancarExcecao() {
        // Arrange
        recorrenciaTeste.setConta(null);
        recorrenciaTeste.setCartaoCredito(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recorrenciaService.criarRecorrencia(recorrenciaTeste);
        });

        assertEquals("Recorrência deve ter conta ou cartão associado", exception.getMessage());
    }

    @Test
    void criarRecorrencia_DataFimAnteriorAInicio_DeveLancarExcecao() {
        // Arrange
        recorrenciaTeste.setDataInicio(LocalDate.now());
        recorrenciaTeste.setDataFim(LocalDate.now().minusDays(1));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recorrenciaService.criarRecorrencia(recorrenciaTeste);
        });

        assertEquals("Data fim deve ser posterior à data início", exception.getMessage());
    }

    @Test
    void executarRecorrenciaManual_RecorrenciaExistente_DeveExecutarComSucesso() {
        // Arrange
        Long recorrenciaId = 1L;

        when(recorrenciaRepository.findById(recorrenciaId))
                .thenReturn(Optional.of(recorrenciaTeste));
        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(contaRepository.save(any(Conta.class)))
                .thenReturn(contaTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.executarRecorrenciaManual(recorrenciaId);

        // Assert
        verify(recorrenciaRepository).findById(recorrenciaId);
        verify(transacaoRepository).save(any(Transacao.class));
        verify(contaRepository).save(any(Conta.class));
        verify(recorrenciaRepository).save(any(TransacaoRecorrente.class));
    }

    @Test
    void executarRecorrenciaManual_RecorrenciaNaoEncontrada_DeveLancarExcecao() {
        // Arrange
        Long recorrenciaId = 999L;
        when(recorrenciaRepository.findById(recorrenciaId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recorrenciaService.executarRecorrenciaManual(recorrenciaId);
        });

        assertEquals("Recorrência não encontrada", exception.getMessage());
    }

    @Test
    void listarRecorrenciasAtivas_UsuarioComRecorrencias_DeveRetornarLista() {
        // Arrange
        Long usuarioId = 1L;
        List<TransacaoRecorrente> recorrenciasEsperadas = Arrays.asList(
                recorrenciaTeste,
                criarRecorrenciaDespesa()
        );

        when(recorrenciaRepository.findByUsuarioIdAndAtivoTrue(usuarioId))
                .thenReturn(recorrenciasEsperadas);

        // Act
        List<TransacaoRecorrente> resultado = recorrenciaService.listarRecorrenciasAtivas(usuarioId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(recorrenciaRepository).findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    @Test
    void desativarRecorrencia_RecorrenciaExistente_DeveDesativarComSucesso() {
        // Arrange
        Long recorrenciaId = 1L;

        when(recorrenciaRepository.findById(recorrenciaId))
                .thenReturn(Optional.of(recorrenciaTeste));
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.desativarRecorrencia(recorrenciaId);

        // Assert
        assertFalse(recorrenciaTeste.getAtivo());
        verify(recorrenciaRepository).save(recorrenciaTeste);
    }

    @Test
    void calcularProximaExecucao_PeriodicidadeMensal_DeveAdicionarUmMes() {
        // Arrange
        LocalDate dataBase = LocalDate.of(2024, 1, 15);
        recorrenciaTeste.setPeriodicidade(Periodicidade.MENSAL);
        recorrenciaTeste.setProximaExecucao(dataBase);

        // Act
        LocalDate resultado = recorrenciaTeste.calcularProximaExecucao();

        // Assert
        assertEquals(LocalDate.of(2024, 2, 15), resultado);
    }

    @Test
    void calcularProximaExecucao_PeriodicidadeSemanal_DeveAdicionarUmaSemana() {
        // Arrange
        LocalDate dataBase = LocalDate.of(2024, 1, 15);
        recorrenciaTeste.setPeriodicidade(Periodicidade.SEMANAL);
        recorrenciaTeste.setProximaExecucao(dataBase);

        // Act
        LocalDate resultado = recorrenciaTeste.calcularProximaExecucao();

        // Assert
        assertEquals(LocalDate.of(2024, 1, 22), resultado);
    }

    @Test
    void processarRecorrencia_ComDataFimDefinida_DeveDesativarQuandoVencer() {
        // Arrange
        recorrenciaTeste.setDataFim(LocalDate.now().minusDays(1)); // Data no passado

        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        assertFalse(recorrenciaTeste.getAtivo());
        verify(recorrenciaRepository).save(recorrenciaTeste);
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    void processarRecorrencia_RecorrenciaCartaoCredito_DeveAtualizarLimite() {
        // Arrange
        recorrenciaTeste.setConta(null);
        recorrenciaTeste.setCartaoCredito(cartaoTeste);
        recorrenciaTeste.setTipo(TipoTransacao.DESPESA);

        BigDecimal limiteOriginal = cartaoTeste.getLimiteUtilizado();

        when(transacaoRepository.save(any(Transacao.class)))
                .thenReturn(new Transacao());
        when(cartaoRepository.save(any(CartaoCredito.class)))
                .thenReturn(cartaoTeste);
        when(recorrenciaRepository.save(any(TransacaoRecorrente.class)))
                .thenReturn(recorrenciaTeste);

        // Act
        recorrenciaService.processarRecorrencia(recorrenciaTeste);

        // Assert
        verify(cartaoRepository).save(argThat(cartao ->
                cartao.getLimiteUtilizado().equals(limiteOriginal.add(recorrenciaTeste.getValor()))
        ));
    }

    // === MÉTODOS AUXILIARES ===

    private TransacaoRecorrente criarRecorrenciaDespesa() {
        TransacaoRecorrente despesa = new TransacaoRecorrente();
        despesa.setId(2L);
        despesa.setDescricao("Aluguel");
        despesa.setValor(new BigDecimal("1200.00"));
        despesa.setTipo(TipoTransacao.DESPESA);
        despesa.setPeriodicidade(Periodicidade.MENSAL);
        despesa.setDataInicio(LocalDate.now().minusMonths(1));
        despesa.setProximaExecucao(LocalDate.now());
        despesa.setAtivo(true);
        despesa.setConta(contaTeste);
        despesa.setCategoria(categoriaTeste);
        despesa.setUsuario(usuarioTeste);
        return despesa;
    }
}