package com.homelab.app.repository;

import com.homelab.app.model.Vale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ValeRepository extends JpaRepository<Vale, Long> {
    List<Vale> findByUsuarioId(Long usuarioId);
    List<Vale> findByUsuarioIdAndSaldoAtualGreaterThan(Long usuarioId, BigDecimal saldo);
}