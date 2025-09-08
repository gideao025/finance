package com.homelab.app.repository;

import com.homelab.app.model.Categoria;
import com.homelab.app.model.TipoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByTipo(TipoCategoria tipo);
}