package org.micro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SenderEmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${email.from}")
    private String emailFrom;

    public void sendSimpleEmail(String body, String subject, String emailTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }
}
