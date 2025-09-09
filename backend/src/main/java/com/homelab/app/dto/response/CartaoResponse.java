package com.homelab.app.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartaoResponse {
    private Long id;
    private String nome;
    private String numeroFinal;
    private BigDecimal limite;
    private BigDecimal limiteUtilizado;
    private BigDecimal limiteDisponivel;
    private LocalDateTime dataVencimento;
    private Boolean ativo;

    public BigDecimal getLimiteDisponivel() {
        return limite.subtract(limiteUtilizado);
    }
}