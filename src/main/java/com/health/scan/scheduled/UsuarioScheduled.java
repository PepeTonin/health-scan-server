package com.health.scan.scheduled;

import com.health.scan.entity.Usuario;
import com.health.scan.repository.IUsuarioRepository;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class UsuarioScheduled {


    private final long SEGUNDO = 1000;
    private final long MINUTO = SEGUNDO * 3600;

    public void expirarCodigo(Usuario usr, IUsuarioRepository repository) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                Usuario usuario = repository.getByEmail(usr.getEmail());
                if(usuario != null && !Objects.equals(usuario.getStatus(), "1")) {
                    usuario.setStatus("2");
                    repository.save(usuario);
                    timer.cancel();
                } else {
                    timer.cancel();
                }
            }
        }, MINUTO, MINUTO);
    }
}