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
@Table(name = "vales")
@Data
@EqualsAndHashCode(callSuper = false)
public class Vale {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoVale tipo;
    
    @DecimalMin(value = "0.0", message = "Saldo inicial deve ser positivo")
    @Column(name = "saldo_inicial", precision = 15, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    
    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;
    
    @Column(name = "validade")
    private LocalDateTime validade;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}

enum TipoVale {
    ALIMENTACAO, COMBUSTIVEL, TRANSPORTE, SAUDE, EDUCACAO
}