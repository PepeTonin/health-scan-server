package com.health.scan.repository;

import com.health.scan.entity.Produto;
import com.health.scan.entity.ProdutoPesquisado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IProdutoRepository extends JpaRepository<ProdutoPesquisado, Long> {

    @Query("SELECT p FROM Produto p "+
            "WHERE p.categoria LIKE %:QUERY% OR p.nome LIKE %:QUERY% OR p.codBarra LIKE %:QUERY% "+
            "ORDER BY p.nome DESC")
    Page<Produto> filtrarProdutos(@Param("QUERY") String query, Pageable pageable);

    @Query("SELECT p FROM Produto p "+
            "WHERE p.codBarra = %:QUERY% ")
    Produto findByCodigo(@Param("QUERY") String query);
}
