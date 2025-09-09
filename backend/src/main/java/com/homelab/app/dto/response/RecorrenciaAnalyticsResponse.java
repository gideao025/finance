package com.homelab.app.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RecorrenciaAnalyticsResponse {
    private BigDecimal totalMensal;
    private BigDecimal totalAnual;
    private Integer quantidadeRecorrencias;
    private List<RecorrenciaPorCategoria> porCategoria;
    private List<ProximasExecucoes> proximasExecucoes;

    @Data
    public static class RecorrenciaPorCategoria {
        private String categoria;
        private BigDecimal valor;
        private Integer quantidade;
    }

    @Data
    public static class ProximasExecucoes {
        private String descricao;
        private BigDecimal valor;
        private String proximaExecucao;
    }
}
