package com.health.scan.service;

import com.health.scan.entity.Produto;
import com.health.scan.entity.ProdutoPesquisado;
import com.health.scan.entity.Usuario;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IProdutoPesquisadoRepository;
import com.health.scan.repository.IProdutoRepository;
import com.health.scan.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class ProdutoPesquisadoService {

    @Autowired
    private IProdutoPesquisadoRepository repository;
    @Autowired
    private IProdutoRepository repositoryProduto;
    @Autowired
    private IUsuarioRepository repositoryUsuario;

    public ObjectResponse buscarUltimosProdutos(Long idUsuario){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Pageable pageable = PageRequest.of(0, 10); // Buscar os primeiros 10 resultados
        Page<ProdutoPesquisado> produtoPesquisado = repository.buscarUltimosProdutos(idUsuario, pageable);
        response.setResult(produtoPesquisado);
        return response;
    }

    public ObjectResponse save(ProdutoPesquisado produtoPesquisado){
        ObjectResponse response = new ObjectResponse();

        if(produtoPesquisado.getProduto() == null || produtoPesquisado.getUsuario() == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
        response.setStatus(HttpStatus.OK);

        Produto produto = repositoryProduto.findByCodigo(produtoPesquisado.getProduto().getCodBarra());
        produtoPesquisado.setProduto(produto);

        if(produtoPesquisado.getProduto() == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

        Optional<Usuario> usuario = repositoryUsuario.findById(produtoPesquisado.getUsuario().getId());
        produtoPesquisado.setUsuario(usuario.get());

        if(produtoPesquisado.getUsuario() == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }

        produtoPesquisado.setDataHoraInc(new Timestamp(System.currentTimeMillis()));
        produtoPesquisado = repository.save(produtoPesquisado);

        response.setResult(produtoPesquisado);
        return response;
    }
}
