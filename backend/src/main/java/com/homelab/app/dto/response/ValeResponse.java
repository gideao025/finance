package com.homelab.app.dto.response;

import com.homelab.app.model.TipoVale;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ValeResponse {
    private Long id;
    private TipoVale tipo;
    private BigDecimal valor;
    private BigDecimal saldoAtual;
    private String descricao;
    private LocalDateTime dataUltimoUso;
    private LocalDateTime dataVencimento;
    private Boolean ativo;
}