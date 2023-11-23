package com.health.scan.controller;

import com.health.scan.entity.ProdutoPesquisado;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.service.ComparacaoService;
import com.health.scan.service.ProdutoPesquisadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ComparacaoController {
    @Autowired
    private ComparacaoService service;

    @RequestMapping(value = "comparacao/buscar-ultimas-comparacoes", method = RequestMethod.GET)
    public ObjectResponse buscarUltimasComparacoes(@RequestParam String idUsuario, @RequestParam String query) {
        return service.buscarUltimasComparacoes(Long.valueOf(idUsuario), query);
    }

    @RequestMapping(value = "comparacao/deletar-comparacao/{idComparacao}", method = RequestMethod.DELETE)
    public ObjectResponse deletarComparacaoById(@PathVariable Long idComparacao) {
        return service.deletarComparacaoById(idComparacao);
    }

}