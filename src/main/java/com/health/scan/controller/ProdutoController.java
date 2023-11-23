package com.health.scan.controller;

import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {
    @Autowired
    private ProdutoService service;

    @RequestMapping(value = "produto/filtrar-produtos", method =  RequestMethod.GET)
    public ObjectResponse filtrarProdutos(@RequestParam String query)
    {
        return service.filtrarProdutos(query);
    }
}