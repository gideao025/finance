package com.homelab.app.dto.response;

import com.homelab.app.model.Periodicidade;
import com.homelab.app.model.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoRecorrenteResponse {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private Periodicidade periodicidade;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate proximaExecucao;
    private Boolean ativo;
    private String nomeConta;
    private String nomeCartao;
    private String nomeCategoria;
    private LocalDateTime criadoEm;

    // Campos calculados
    private Long diasParaProximaExecucao;
    private String statusProximaExecucao;
}