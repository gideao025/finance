package com.homelab.app.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CartaoRequest {
    
    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;
    
    @NotBlank(message = "Nome do cartão é obrigatório")
    private String nome;
    
    @DecimalMin(value = "0.01", message = "Limite deve ser maior que zero")
    @NotNull(message = "Limite total é obrigatório")
    private BigDecimal limiteTotal;
    
    private LocalDate vencimento;
}