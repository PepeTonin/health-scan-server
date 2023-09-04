package com.health.scan.service;

import com.health.scan.entity.Usuario;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IUsuarioRepository;
import com.health.scan.util.email.EmailSend;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UsuarioService{

    @Autowired
    private IUsuarioRepository repository;

    public ObjectResponse logar(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);
        Usuario usuario = repository.getLogin(usr.getLogin(), usr.getSenha());

        if(usuario != null && Objects.equals(usuario.getStatus(), "0")){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(usuario);
            response.setMessage("usuario-nao-validado");
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
        }
        response.setResult(usuario);

        if(response.getResult() == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("usuario-senha-nao-encontrado");
        }
        return response;
    }

    public ObjectResponse cadastrarUsuario(Usuario usr
    ){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);

        Usuario usuario;

        usuario = repository.loginExist(usr.getLogin());
        if (usuario != null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("login-ja-existe");
            return response;
        }

        usuario = repository.emailExist(usr.getEmail());
        if (usuario != null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("email-ja-existe");
            return response;
        }

        gerarToken(usr);
        usuario = gerarCodigo(usr);

        response.setResult(usuario);

        return response;
    }

    public ObjectResponse validarCodigo(Usuario usr){
        ObjectResponse response = new ObjectResponse();
        response.setStatus(HttpStatus.OK);
        Usuario usuario = repository.getValidator(usr.getLogin(), usr.getEmail(), usr.getValidator());

        response.setResult(usuario);

        if(response.getResult() == null){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setMessage("codigo-incorreto");
            return response;
        }

        usuario.setStatus("1");
        usuario.setValidator(null);
        repository.save(usuario);
        return response;
    }

    private Usuario gerarCodigo(Usuario usr){
        int max = 99999;
        int min = 10000;
        int intervalo = max - min + 1;
        String validator = String.valueOf((int)(Math.random() * intervalo) + min);

        usr.setValidator(validator);
        usr.setStatus("0");

        EmailSend emailSend = new EmailSend();
        emailSend.send(usr.getEmail(), "Health Scan - Codigo de verificação", "Seu código de verificação para o aplicativo HealthScan é: "+usr.getValidator());

        return repository.save(usr);
    }

    private Usuario gerarToken(Usuario usuario){
        UUID token = UUID.randomUUID();
        String uutoken = token.toString();
        usuario.setToken(uutoken);

        return usuario;
    }
}
