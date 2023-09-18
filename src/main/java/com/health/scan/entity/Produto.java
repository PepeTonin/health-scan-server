package com.health.scan.entity;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "PRODUTO")
public class Produto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRODUTO")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Column(name = "CODBARRA")
    private String codBarra;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "DATAHORAINC")
    private Timestamp dataHoraInc;

    @Column(name = "DATAHORAALT")
    private Timestamp dataHoraAlt;

    @Column(name = "DATAHORADEL")
    private Timestamp dataHoraDel;

    // getters e setters
    // construtores

    // Outros métodos e lógica da classe, se necessário

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
