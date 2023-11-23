package com.health.scan.dto;

import com.health.scan.entity.Produto;

public class ProdutoComparadoDTO {

    private Long id;

    private Produto produto;

    private Long idcomparacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Long getIdcomparacao() {
        return idcomparacao;
    }

    public void setIdcomparacao(Long idcomparacao) {
        this.idcomparacao = idcomparacao;
    }
}
