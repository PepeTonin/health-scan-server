package com.health.scan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.health.scan.entity.ProdutoPesquisado;

public interface IProdutoPesquisadoRepository extends JpaRepository<ProdutoPesquisado, Long> {

    @Query("SELECT pp FROM ProdutoPesquisado pp "
            + "INNER JOIN FETCH pp.produto p "
            + "WHERE pp.usuario.id = :idUsuario "
            + "ORDER BY pp.dataHoraInc DESC")
    Page<ProdutoPesquisado> buscarUltimosProdutos(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
