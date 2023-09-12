package com.health.scan.service;

import com.health.scan.entity.Usuario;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IUsuarioRepository;
import com.health.scan.scheduled.UsuarioScheduled;
import com.health.scan.util.email.EmailSend;
import com.health.scan.util.file.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class UsuarioService{

    @Autowired
    private IUsuarioRepository repository;

    public ObjectResponse logar(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);
        Usuario usuario = repository.getLogin(usr.getLogin(), usr.getSenha());

        if(usuario != null && (Objects.equals(usuario.getStatus(), "0") )){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(usuario);
            response.setMessage("usuario-nao-validado");
            return response;
        }

        if(usuario != null && Objects.equals(usuario.getStatus(), "2")) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(usuario);
            response.setMessage("codigo-expirado");
            return response;
        }

            if (usuario != null) {
            gerarToken(usuario);
            repository.saveAndFlush(usuario);
        }

        response.setResult(usuario);

        if(response.getResult() == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("usuario-senha-nao-encontrado");
        }

        return response;
    }

    public ObjectResponse logarToken(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);
        Usuario usuario = repository.getLoginToken(usr.getLogin(), usr.getToken());

        if (usuario != null) {
            gerarToken(usuario);
            repository.save(usuario);
            usuario.setSenha("");
        }
        response.setResult(usuario);

        if(response.getResult() == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("usuario-senha-nao-encontrado");
        }
        return response;
    }

    public ObjectResponse cadastrarNovaSenha(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Usuario usuario = repository.getByEmail(usr.getEmail());
        usuario.setSenha(usr.getSenha());

        usuario = repository.save(usuario);
        usuario.setSenha("");
        response.setResult(usuario);

        return response;
    }

    public ObjectResponse cadastrarUsuario(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Usuario usuario;

        usuario = repository.loginExist(usr.getLogin());
        if (usuario != null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("login-ja-existe");
            return response;
        }

        usuario = repository.getByEmail(usr.getEmail());
        if (usuario != null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("email-ja-existe");
            return response;
        }

        String html = gerarHtmlEmailValidacao(usr);
        usuario = gerarCodigo(usr, "Health Scan - Codigo de verificação", html);

        usuario.setSenha("");
        response.setResult(usuario);

        return response;
    }

    public ObjectResponse validarCodigo(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);
        Usuario usuario = repository.getValidator(usr.getEmail(), usr.getValidator());

        if(usuario == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("codigo-incorreto");
            return response;
        }

        if(usuario.getStatus() != null && usuario.getStatus().equals("2")){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("codigo-expirado");
            return response;
        }

        usuario.setStatus("1");
        usuario.setValidator(null);
        repository.save(usuario);
        usuario.setSenha("");

        response.setResult(usuario);
        return response;
    }

    private Usuario gerarCodigo(Usuario usr, String titulo, String mensagem){
        gerarToken(usr);

        int max = 99999;
        int min = 10000;
        int intervalo = max - min + 1;
        String validator = String.valueOf((int)(Math.random() * intervalo) + min);
        mensagem = mensagem.replace("id:codigo", validator);

        usr.setValidator(validator);
        usr.setStatus("0");

        EmailSend emailSend = new EmailSend();
        emailSend.sendHtml(usr.getEmail(), titulo, mensagem);

        UsuarioScheduled usuarioScheduled = new UsuarioScheduled();
        usuarioScheduled.expirarCodigo(usr, repository);

        return repository.save(usr);
    }

    public ObjectResponse enviarEmailRecuperacao(Usuario usr){
        Usuario usuario = repository.getByEmail(usr.getEmail());

        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        if(usuario == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("email-nao-existe");
            return response;
        }

        String html = gerarHtmlEmailRecuperacao(usuario);

        usuario = gerarCodigo(usuario, "Health Scan - Codigo de recuperação", html);
        usuario.setSenha("");
        response.setResult(usuario);

        return response;
    }

    public ObjectResponse reenviarEmailValidacao(Usuario usr){
        Usuario usuario = repository.getByEmail(usr.getEmail());

        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        if(usuario == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("email-nao-existe");
            return response;
        }
        String html = gerarHtmlEmailValidacao(usuario);

        usuario = gerarCodigo(usuario, "Health Scan - Codigo de verificação", html);
        usuario.setSenha("");
        response.setResult(usuario);

        return response;
    }
    private String gerarHtmlEmailRecuperacao(Usuario usuario) {
        String html = "";
        try {
            FileUtil fileUtil = new FileUtil();
            String imagemModeloEscuro = fileUtil.toBase64("./modelos/email-recuperacao/assets/modelo_escuro.png", "image/png");
            String imagemModeloEscuroLogo = fileUtil.toBase64("./modelos/email-recuperacao/assets/modelo_escuro_logo.png", "image/png");

            html = lerArquivo("./modelos/email-recuperacao/index.html");

            html = html.replace("id:modelo-escuro", imagemModeloEscuro);
            html = html.replace("id:modelo-logo-escuro", imagemModeloEscuroLogo);
            html = html.replace("id:nome-usuario", usuario.getLogin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return html;
    }
    private String gerarHtmlEmailValidacao(Usuario usuario) {
        String html = "";
        html = lerArquivo("./modelos/email-validacao/index.html");
        html = html.replace("id:nome-usuario", usuario.getLogin());

        return html;
    }

    private String lerArquivo(String path){
        StringBuilder html = new StringBuilder("");
        try {
            FileReader arq = new FileReader(path);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = "";
            while (linha != null) {
                linha = lerArq.readLine();
                html.append(linha);
            }
            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
        }
        return html.toString();
    }

    private Usuario gerarToken(Usuario usuario){
        UUID token = UUID.randomUUID();
        String uutoken = token.toString();
        usuario.setToken(uutoken);

        return usuario;
    }
}
