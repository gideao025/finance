package com.homelab.app.dto.response;

import com.homelab.app.model.TipoRecorrencia;
import com.homelab.app.model.TipoTransacao;
import com.homelab.app.model.Transacao;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransacaoResponse {
    private Long id;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private LocalDateTime data;
    private TipoRecorrencia recorrencia;
    private String descricao;
    private String categoriaNome;
    private String origemNome; // Nome da conta ou cartão
    private String origemTipo; // "CONTA" ou "CARTAO"
    
    public static TransacaoResponse fromEntity(Transacao transacao) {
        return TransacaoResponse.builder()
            .id(transacao.getId())
            .valor(transacao.getValor())
            .tipo(transacao.getTipo())
            .data(transacao.getData())
            .recorrencia(transacao.getRecorrencia())
            .descricao(transacao.getDescricao())
            .categoriaNome(transacao.getCategoria().getNome())
            .origemNome(transacao.getConta() != null ? 
                transacao.getConta().getNome() : 
                transacao.getCartaoCredito().getNome())
            .origemTipo(transacao.getConta() != null ? "CONTA" : "CARTAO")
            .build();
    }
}