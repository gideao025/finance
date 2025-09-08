package com.homelab.app.dto.response;

import com.homelab.app.model.TipoVale;
import com.homelab.app.model.Vale;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SaldoValeResponse {
    private Long valeId;
    private TipoVale tipo;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private BigDecimal saldoUtilizado;
    private LocalDateTime validade;
    private Boolean expirado;
    
    public static SaldoValeResponse fromEntity(Vale vale) {
        BigDecimal saldoUtilizado = vale.getSaldoInicial().subtract(vale.getSaldoAtual());
        Boolean expirado = vale.getValidade() != null && 
            vale.getValidade().isBefore(LocalDateTime.now());
            
        return SaldoValeResponse.builder()
            .valeId(vale.getId())
            .tipo(vale.getTipo())
            .saldoInicial(vale.getSaldoInicial())
            .saldoAtual(vale.getSaldoAtual())
            .saldoUtilizado(saldoUtilizado)
            .validade(vale.getValidade())
            .expirado(expirado)
            .build();
    }
}