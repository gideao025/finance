package com.homelab.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contas")
@Data
@EqualsAndHashCode(callSuper = false)
public class Conta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    @NotBlank(message = "Nome da conta é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @DecimalMin(value = "0.0", message = "Saldo inicial deve ser positivo")
    @Column(name = "saldo_inicial", precision = 15, scale = 2)
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    
    @Column(name = "saldo_atual", precision = 15, scale = 2)
    private BigDecimal saldoAtual = BigDecimal.ZERO;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    // Relacionamentos
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transacao> transacoes;
}