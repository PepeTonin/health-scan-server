package com.health.scan.repository;

import com.health.scan.entity.Comparacao;
import com.health.scan.entity.ProdutoPesquisado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IComparacaoRepository extends JpaRepository<Comparacao, Long> {

    @Query("SELECT c FROM Comparacao c "
            + "INNER JOIN FETCH c.usuario u "
            + "WHERE c.usuario.id = :idUsuario "
            + "AND c.nome LIKE %:QUERY%")
    Page<Comparacao> buscarUltimasComparacoes(@Param("idUsuario") Long idUsuario, @Param("QUERY") String query, Pageable pageable);
}
