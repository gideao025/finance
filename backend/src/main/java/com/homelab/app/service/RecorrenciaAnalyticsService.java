package com.homelab.app.service;

import com.homelab.app.dto.response.RecorrenciaAnalyticsResponse;
import com.homelab.app.model.TransacaoRecorrente;
import com.homelab.app.repository.TransacaoRecorrenteRepository;
import com.homelab.app.repository.TransacaoRepository;
import com.homelab.app.model.Periodicidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecorrenciaAnalyticsService {

    private final TransacaoRecorrenteRepository recorrenciaRepository;
    private final TransacaoRepository transacaoRepository;

    /**
     * Gerar relatório de análise de recorrências
     */
    public RecorrenciaAnalyticsResponse gerarAnaliseRecorrencias(Long usuarioId) {
        List<TransacaoRecorrente> recorrencias =
                recorrenciaRepository.findByUsuarioIdAndAtivoTrue(usuarioId);

        RecorrenciaAnalyticsResponse analytics = new RecorrenciaAnalyticsResponse();

        // Calcular receitas recorrentes mensais
        BigDecimal receitasMensais = calcularReceitasMensais(recorrencias);
        analytics.setReceitasMensaisRecorrentes(receitasMensais);

        // Calcular despesas recorrentes mensais
        BigDecimal despesasMensais = calcularDespesasMensais(recorrencias);
        analytics.setDespesasMensaisRecorrentes(despesasMensais);

        // Fluxo líquido mensal
        analytics.setFluxoLiquidoMensal(receitasMensais.subtract(despesasMensais));

        // Próximas execuções
        analytics.setProximasExecucoes(obterProximasExecucoes(recorrencias));

        // Distribuição por categoria
        analytics.setDistribuicaoPorCategoria(
                calcularDistribuicaoPorCategoria(recorrencias));

        return analytics;
    }

    private BigDecimal calcularReceitasMensais(List<TransacaoRecorrente> recorrencias) {
        return recorrencias.stream()
                .filter(r -> r.getTipo() == TipoTransacao.RECEITA)
                .map(this::normalizarValorMensal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularDespesasMensais(List<TransacaoRecorrente> recorrencias) {
        return recorrencias.stream()
                .filter(r -> r.getTipo() == TipoTransacao.DESPESA)
                .map(this::normalizarValorMensal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal normalizarValorMensal(TransacaoRecorrente recorrencia) {
        return switch (recorrencia.getPeriodicidade()) {
            case SEMANAL -> recorrencia.getValor().multiply(new BigDecimal("4.33")); // 4.33 semanas/mês
            case MENSAL -> recorrencia.getValor();
            case TRIMESTRAL -> recorrencia.getValor().divide(new BigDecimal("3"));
            case ANUAL -> recorrencia.getValor().divide(new BigDecimal("12"));
        };
    }

    private List<ProximaExecucaoInfo> obterProximasExecucoes(List<TransacaoRecorrente> recorrencias) {
        return recorrencias.stream()
                .map(r -> new ProximaExecucaoInfo(
                        r.getDescricao(),
                        r.getValor(),
                        r.getProximaExecucao(),
                        r.getPeriodicidade()
                ))
                .sorted((a, b) -> a.getData().compareTo(b.getData()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> calcularDistribuicaoPorCategoria(
            List<TransacaoRecorrente> recorrencias) {

        return recorrencias.stream()
                .filter(r -> r.getTipo() == TipoTransacao.DESPESA)
                .collect(Collectors.groupingBy(
                        r -> r.getCategoria().getNome(),
                        Collectors.mapping(
                                this::normalizarValorMensal,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }
}

// Classe auxiliar para próximas execuções
class ProximaExecucaoInfo {
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private Periodicidade periodicidade;

    public ProximaExecucaoInfo(@NotBlank(message = "Descrição é obrigatória") String descricao, @NotNull(message = "Valor é obrigatório") @Positive(message = "Valor deve ser positivo") BigDecimal valor, @NotNull(message = "Próxima execução é obrigatória") LocalDate proximaExecucao, Periodicidade periodicidade) {

    }

    // Constructors, getters e setters...
}