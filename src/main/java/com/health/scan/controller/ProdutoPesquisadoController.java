package com.health.scan.controller;

import com.health.scan.entity.ProdutoPesquisado;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.service.ProdutoPesquisadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProdutoPesquisadoController {
    @Autowired
    private ProdutoPesquisadoService service;

    @RequestMapping(value = "produto-pesquisado/buscar-ultimos-produtos", method =  RequestMethod.GET)
    public ObjectResponse buscarUltimosProdutos(@RequestParam String idUsuario)
    {
        return service.buscarUltimosProdutos(Long.valueOf(idUsuario));
    }

    @RequestMapping(value = "produto-pesquisado/save", method =  RequestMethod.POST)
    public ObjectResponse save(@RequestBody ProdutoPesquisado produtoPesquisado)
    {
        return service.save(produtoPesquisado);
    }
}