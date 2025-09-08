package com.homelab.app.repository;

import com.homelab.app.model.Transacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    
    @Query("""
        SELECT t FROM Transacao t 
        WHERE (:usuarioId IS NULL OR t.conta.usuario.id = :usuarioId OR t.cartaoCredito.usuario.id = :usuarioId)
        AND (:contaId IS NULL OR t.conta.id = :contaId)
        AND (:cartaoId IS NULL OR t.cartaoCredito.id = :cartaoId)  
        AND (:categoriaId IS NULL OR t.categoria.id = :categoriaId)
        ORDER BY t.data DESC
        """)
    Page<Transacao> findByFilters(
        @Param("usuarioId") Long usuarioId,
        @Param("contaId") Long contaId, 
        @Param("cartaoId") Long cartaoId,
        @Param("categoriaId") Long categoriaId,
        Pageable pageable
    );
    
    @Query("""
        SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t 
        WHERE (t.conta.usuario.id = :usuarioId OR t.cartaoCredito.usuario.id = :usuarioId)
        AND t.tipo = 'RECEITA'
        """)
    BigDecimal sumReceitasByUsuarioId(@Param("usuarioId") Long usuarioId);
    
    @Query("""
        SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t 
        WHERE (t.conta.usuario.id = :usuarioId OR t.cartaoCredito.usuario.id = :usuarioId)
        AND t.tipo = 'DESPESA'
        """)
    BigDecimal sumDespesasByUsuarioId(@Param("usuarioId") Long usuarioId);
}