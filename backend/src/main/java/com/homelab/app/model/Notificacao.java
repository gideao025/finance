package com.homelab.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Data
@EqualsAndHashCode(callSuper = false)
public class Notificacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull
    private Usuario usuario;
    
    @NotBlank(message = "Mensagem é obrigatória")
    @Column(name = "mensagem", nullable = false, length = 1000)
    private String mensagem;
    
    @Column(name = "lida", nullable = false)
    private Boolean lida = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoNotificacao tipo;
    
    @CreationTimestamp
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    
    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;
}

