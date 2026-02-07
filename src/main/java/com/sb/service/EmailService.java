package com.sb.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordEmail(String toEmail, String tempPassword) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Temporary Password");
        message.setText(
            "Welcome to Event Management System\n\n" +
            "Your temporary password is: " + tempPassword + "\n\n" +
            "Please login and change your password immediately."
        );

        mailSender.send(message);
    }
}
