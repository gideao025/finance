package com.homelab.app.dto.request;

import com.homelab.app.model.TipoRecorrencia;
import com.homelab.app.model.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransacaoRequest {
    
    private Long contaId;
    private Long cartaoId;
    
    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;
    
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @NotNull(message = "Valor é obrigatório")
    private BigDecimal valor;
    
    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;
    
    private LocalDateTime data;
    private TipoRecorrencia recorrencia;
    private String descricao;
}