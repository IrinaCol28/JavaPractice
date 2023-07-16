package com.example.practice.services;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    public void sendEmail(String recipientEmail, String subject, String content) {
        String host = "smtp.elasticemail.com";
        int port = 2525;
        String username = "irina.col2828@gmail.com";
        String password = "D8CA5E117455C883BFF7AAB9664267C80115";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
            log.info("Email send to {}", recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
           log.info("Email not send");

        }
    }
}