package com.homelab.app.dto.request;

import com.homelab.app.model.Periodicidade;
import com.homelab.app.model.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoRecorrenteRequest {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "Periodicidade é obrigatória")
    private Periodicidade periodicidade;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    private Long contaId;

    private Long cartaoCreditoId;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;
}