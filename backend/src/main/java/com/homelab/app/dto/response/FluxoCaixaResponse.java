package com.homelab.app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FluxoCaixaResponse {
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
    private BigDecimal saldoProjetado;
    private BigDecimal percentualGastos;
}