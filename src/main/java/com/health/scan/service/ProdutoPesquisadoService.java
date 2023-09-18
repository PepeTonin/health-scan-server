package com.health.scan.service;

import com.health.scan.entity.ProdutoPesquisado;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IProdutoPesquisadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProdutoPesquisadoService {

    @Autowired
    private IProdutoPesquisadoRepository repository;

    public ObjectResponse buscarUltimosProdutos(Long idUsuario){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Pageable pageable = PageRequest.of(0, 10); // Buscar os primeiros 10 resultados
        Page<ProdutoPesquisado> produtoPesquisado = repository.buscarUltimosProdutos(idUsuario, pageable);
        response.setResult(produtoPesquisado);
        return response;
    }
}
