package com.homelab.app.dto.request;

import com.homelab.app.model.TipoVale;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ValeRequest {
    
    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;
    
    @NotNull(message = "Tipo do vale é obrigatório")
    private TipoVale tipo;
    
    @DecimalMin(value = "0.01", message = "Saldo inicial deve ser maior que zero")
    @NotNull(message = "Saldo inicial é obrigatório")
    private BigDecimal saldoInicial;
    
    private LocalDateTime validade;
}