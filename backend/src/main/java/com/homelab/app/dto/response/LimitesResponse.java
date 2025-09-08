package com.homelab.app.dto.response;

import com.homelab.app.model.CartaoCredito;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LimitesResponse {
    private Long cartaoId;
    private String nome;
    private BigDecimal limiteTotal;
    private BigDecimal limiteDisponivel;
    private BigDecimal limiteUtilizado;
    private LocalDate vencimento;
    private BigDecimal percentualUtilizacao;
    
    public static LimitesResponse fromEntity(CartaoCredito cartao) {
        BigDecimal limiteUtilizado = cartao.getLimiteTotal().subtract(cartao.getLimiteDisponivel());
        BigDecimal percentual = limiteUtilizado.multiply(BigDecimal.valueOf(100))
            .divide(cartao.getLimiteTotal(), 2, RoundingMode.HALF_UP);
            
        return LimitesResponse.builder()
            .cartaoId(cartao.getId())
            .nome(cartao.getNome())
            .limiteTotal(cartao.getLimiteTotal())
            .limiteDisponivel(cartao.getLimiteDisponivel())
            .limiteUtilizado(limiteUtilizado)
            .vencimento(cartao.getVencimento())
            .percentualUtilizacao(percentual)
            .build();
    }
}


