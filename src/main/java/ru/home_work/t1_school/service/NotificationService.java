package ru.home_work.t1_school.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
public class NotificationService {

    private String from = "my_out_mail";
    private String to = "my_in_mail";
    private String subject = "Task Updated";
    private String text = "Task id %s updated";
    private String password = "password";

    public void sendNotification(Long id) throws MailException {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.yandex.ru"); // smtp сервер Google
        properties.put("mail.smtp.port", "465 ");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(String.format(text, id));
            Transport.send(message);
            log.info("Sent message successfully");
        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
