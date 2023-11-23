package com.health.scan.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "COMPARACAO")
public class Comparacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDCOMPARACAO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO")
    private Usuario usuario;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DATAHORAINC")
    private Timestamp dataHoraInc;

    @Column(name = "DATAHORAALT")
    private Timestamp dataHoraAlt;

    @Column(name = "DATAHORADEL")
    private Timestamp dataHoraDel;

    @Transient
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<ProdutoComparado> produtoComparadoList;

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

    public List<ProdutoComparado> getProdutoComparadoList() {
        return produtoComparadoList;
    }

    public void setProdutoComparadoList(List<ProdutoComparado> produtoComparadoList) {
        this.produtoComparadoList = produtoComparadoList;
    }
}