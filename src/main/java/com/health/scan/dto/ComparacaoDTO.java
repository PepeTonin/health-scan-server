package com.health.scan.dto;
import com.health.scan.entity.Usuario;

import java.sql.Timestamp;
import java.util.List;

public class ComparacaoDTO {

    private Long id;

    private Usuario usuario;

    private String nome;

    private Timestamp dataHoraInc;

    private Timestamp dataHoraAlt;

    private Timestamp dataHoraDel;

    private List<ProdutoComparadoDTO> produtoComparadoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Timestamp getDataHoraInc() {
        return dataHoraInc;
    }

    public void setDataHoraInc(Timestamp dataHoraInc) {
        this.dataHoraInc = dataHoraInc;
    }

    public Timestamp getDataHoraAlt() {
        return dataHoraAlt;
    }

    public void setDataHoraAlt(Timestamp dataHoraAlt) {
        this.dataHoraAlt = dataHoraAlt;
    }

    public Timestamp getDataHoraDel() {
        return dataHoraDel;
    }

    public void setDataHoraDel(Timestamp dataHoraDel) {
        this.dataHoraDel = dataHoraDel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ProdutoComparadoDTO> getProdutoComparadoList() {
        return produtoComparadoList;
    }

    public void setProdutoComparadoList(List<ProdutoComparadoDTO> produtoComparadoList) {
        this.produtoComparadoList = produtoComparadoList;
    }
}