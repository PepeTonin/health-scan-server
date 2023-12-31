package com.health.scan.repository;

import com.health.scan.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.login = ?1 AND u.senha = ?2")
    Usuario getLogin(String login, String pass);

    @Query("SELECT u FROM Usuario u WHERE u.email = ?1 AND u.validator = ?2")
    Usuario getValidator(String email, String code);

    @Query("SELECT u FROM Usuario u WHERE u.login = ?1")
    Usuario loginExist(String login);

    @Query("SELECT u FROM Usuario u WHERE u.email = ?1")
    Usuario getByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.login = ?1 AND u.token = ?2")
    Usuario getLoginToken(String title, String token);
}