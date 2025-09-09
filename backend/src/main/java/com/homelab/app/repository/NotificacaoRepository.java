package com.homelab.app.repository;

import com.homelab.app.model.Notificacao;
import com.homelab.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuario(Usuario usuario);

    List<Notificacao> findByUsuarioAndLidaFalse(Usuario usuario);
}
