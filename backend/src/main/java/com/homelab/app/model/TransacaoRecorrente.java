package com.homelab.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes_recorrentes")
@Data
@EqualsAndHashCode(callSuper = false)
public class TransacaoRecorrente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(name = "descricao", nullable = false)
    private String descricao;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTransacao tipo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "periodicidade", nullable = false)
    private Periodicidade periodicidade;
    
    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    
    @Column(name = "data_fim")
    private LocalDate dataFim;
    
    @NotNull(message = "Próxima execução é obrigatória")
    @Column(name = "proxima_execucao", nullable = false)
    private LocalDate proximaExecucao;
    
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Conta conta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    // Métodos auxiliares
    public LocalDate calcularProximaExecucao() {
        return switch (this.periodicidade) {
            case SEMANAL -> this.proximaExecucao.plusWeeks(1);
            case MENSAL -> this.proximaExecucao.plusMonths(1);
            case TRIMESTRAL -> this.proximaExecucao.plusMonths(3);
            case ANUAL -> this.proximaExecucao.plusYears(1);
        };
    }
}

