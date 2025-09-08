package com.homelab.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Data
@EqualsAndHashCode(callSuper = false)
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    private Conta conta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartao_credito_id")
    private CartaoCredito cartaoCredito;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull
    private Categoria categoria;
    
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Column(name = "valor", precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoTransacao tipo;
    
    @Column(name = "data", nullable = false)
    private LocalDateTime data;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "recorrencia")
    private TipoRecorrencia recorrencia;
    
    @Column(name = "descricao", length = 500)
    private String descricao;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    // Validação: Uma transação deve ter conta OU cartão, mas não ambos
    @PrePersist
    @PreUpdate
    private void validarOrigem() {
        if ((conta == null && cartaoCredito == null) || 
            (conta != null && cartaoCredito != null)) {
            throw new IllegalArgumentException(
                "Transação deve ter uma conta OU um cartão de crédito"
            );
        }
    }
}

enum TipoTransacao {
    RECEITA, DESPESA, TRANSFERENCIA
}

enum TipoRecorrencia {
    NENHUMA, DIARIA, SEMANAL, MENSAL, BIMESTRAL, 
    TRIMESTRAL, SEMESTRAL, ANUAL
}