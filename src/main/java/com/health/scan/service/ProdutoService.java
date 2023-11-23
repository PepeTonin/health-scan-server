package com.health.scan.service;

import com.health.scan.entity.Produto;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    private IProdutoRepository repository;

    public ObjectResponse filtrarProdutos(String query){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Pageable pageable = PageRequest.of(0, 50); // Buscar os primeiros 10 resultados
        Page<Produto> produto = repository.filtrarProdutos(query, pageable);
        response.setResult(produto);
        return response;
    }
}
