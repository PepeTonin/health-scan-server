package com.health.scan.repository;

import com.health.scan.entity.ProdutoComparado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProdutoComparadoRepository extends JpaRepository<ProdutoComparado, Long> {

    @Query("SELECT pc FROM ProdutoComparado pc "
            + "INNER JOIN FETCH pc.comparacao u "
            + "WHERE pc.comparacao.id in (:idsComparacao) ")
    List<ProdutoComparado> buscarPorIdComparacao(@Param("idsComparacao") List<Long> idsComparacao);

    @Modifying
    @Query("DELETE FROM ProdutoComparado pc WHERE pc.comparacao.id = :idComparacao")
    void deleteByIdComparacao(@Param("idComparacao") Long idComparacao);
}
