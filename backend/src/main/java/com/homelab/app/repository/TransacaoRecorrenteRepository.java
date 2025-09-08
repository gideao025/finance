package com.homelab.app.repository;

import com.homelab.app.model.TransacaoRecorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRecorrenteRepository extends JpaRepository<TransacaoRecorrente, Long> {
    
    @Query("""
        SELECT tr FROM TransacaoRecorrente tr 
        WHERE tr.ativo = true 
        AND tr.proximaExecucao <= :dataAtual
        """)
    List<TransacaoRecorrente> findRecorrenciasParaProcessar(@Param("dataAtual") LocalDate dataAtual);
    
    @Query("""
        SELECT tr FROM TransacaoRecorrente tr 
        WHERE tr.usuario.id = :usuarioId 
        AND tr.ativo = true
        ORDER BY tr.proximaExecucao ASC
        """)
    List<TransacaoRecorrente> findByUsuarioIdAndAtivoTrue(@Param("usuarioId") Long usuarioId);
    
    @Query("""
        SELECT tr FROM TransacaoRecorrente tr 
        WHERE tr.descricao ILIKE %:descricao% 
        AND tr.usuario.id = :usuarioId
        AND tr.periodicidade = :periodicidade
        """)
    List<TransacaoRecorrente> findSimilarRecorrencias(
        @Param("descricao") String descricao,
        @Param("usuarioId") Long usuarioId,
        @Param("periodicidade") String periodicidade
    );
}