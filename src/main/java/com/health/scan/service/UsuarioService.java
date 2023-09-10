package com.health.scan.service;

import com.health.scan.entity.Usuario;
import com.health.scan.entity.response.ObjectResponse;
import com.health.scan.repository.IUsuarioRepository;
import com.health.scan.scheduled.UsuarioScheduled;
import com.health.scan.util.email.EmailSend;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

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

        usuario = gerarCodigo(usr, "Health Scan - Codigo de verificação", "Seu código de verificação para o aplicativo HealthScan é: ");
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

        usr.setValidator(validator);
        usr.setStatus("0");

        EmailSend emailSend = new EmailSend();
        emailSend.send(usr.getEmail(), titulo, mensagem+usr.getValidator());

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

        usuario = gerarCodigo(usuario, "Health Scan - Recuperação de senha", "Seu Código para recuperação de senha é: ");
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

        usuario = gerarCodigo(usuario, "Health Scan - Codigo de verificação", "Seu código de verificação para o aplicativo HealthScan é: ");
        usuario.setSenha("");
        response.setResult(usuario);

        return response;
    }
    private Usuario gerarToken(Usuario usuario){
        UUID token = UUID.randomUUID();
        String uutoken = token.toString();
        usuario.setToken(uutoken);

        return usuario;
    }
}
