package com.example.practice.services;

import com.example.practice.controllers.CustomerController;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    public void sendEmail(String recipientEmail, String subject, String content) {
        // Настройте свои параметры SMTP-сервера
        String host = "smtp.elasticemail.com";
        int port = 2525 ;
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
            LOG.info("Email send to {}", recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            LOG.info("Email not send");

        }
    }
}