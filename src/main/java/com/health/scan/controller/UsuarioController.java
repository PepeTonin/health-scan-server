package com.health.scan.controller;

import com.health.scan.entity.Usuario;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @RequestMapping(value = "usuario/enviar-email-recuperacao", method =  RequestMethod.POST)
    public ObjectResponse enviarEmailRecuperacao(@RequestBody Usuario usuario)
    {
        return usuarioService.enviarEmailRecuperacao(usuario);
    }

    @RequestMapping(value = "usuario/reenviar-email-validacao", method =  RequestMethod.POST)
    public ObjectResponse reenviarEmailValidacao(@RequestBody Usuario usuario)
    {
        return usuarioService.reenviarEmailValidacao(usuario);
    }

    @RequestMapping(value = "usuario/logar", method =  RequestMethod.POST)
    public ObjectResponse post(@RequestBody Usuario usuario)
    {
        return usuarioService.logar(usuario);
    }

    @RequestMapping(value = "usuario/logar-token", method =  RequestMethod.POST)
    public ObjectResponse postToken(@Validated @RequestBody Usuario usuario)
    {
        return usuarioService.logarToken(usuario);
    }

    @RequestMapping(value = "usuario/cadastrar-usuario", method =  RequestMethod.POST)
    public ObjectResponse cadastrarUsuario(@RequestBody Usuario usuario)
    {
        return usuarioService.cadastrarUsuario(usuario);
    }

    @RequestMapping(value = "usuario/validar-codigo", method =  RequestMethod.POST)
    public ObjectResponse validarCodigo(@RequestBody Usuario usuario)
    {
        return usuarioService.validarCodigo(usuario);
    }

    @RequestMapping(value = "usuario/cadastrar-nova-senha", method =  RequestMethod.POST)
    public ObjectResponse cadastrarNovaSenha(@RequestBody Usuario usuario)
    {
        return usuarioService.cadastrarNovaSenha(usuario);
    }
}