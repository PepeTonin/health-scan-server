package com.health.scan.util.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSend {
    private static String EMAIL = "potatonaointeeressa@gmail.com";
    private static String PASSWORD = "reszbefqbomaicon";

    public void sendText(String email, String assunto, String mensagem) {
        try {
            Session session = generateSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));

            Address[] toUser = InternetAddress.parse(email);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(assunto);//Assunto
            message.setText(mensagem);

            /**Método para enviar a mensagem criada*/
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendHtml(String email, String assunto, String html) {
        try {
            Session session = generateSession();
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));

            Address[] toUser = InternetAddress.parse(email);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(assunto);//Assunto
            message.setContent(html, "text/html; charset=UTF-8");

            /**Método para enviar a mensagem criada*/
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private Session generateSession(){
        Properties props = new Properties();

        /** Parâmetros de conexão com servidor Gmail */
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port","465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.connectiontimeout","60000");
            props.put("mail.smtp.auth","true");
            props.put("mail.smtp.port","465");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL, PASSWORD);
                }
        });

        /** Ativa Debug para sessão */
        //session.setDebug(true);
        return session;
    }
}
