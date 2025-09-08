package com.homelab.app.repository;

import com.homelab.app.model.CartaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioId(Long usuarioId);
}