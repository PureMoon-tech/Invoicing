package com.example.invc_proj.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to BillMitra!");
        message.setText("Hi " + userName + ",\n\nWelcome to BillMitra! We're excited to have you on board." +
                                             "\n\nBest Regards," +
                                             "\nPureMoon-Tech");
        mailSender.send(message);
    }

    public void sendResetEmail(String email, String token) {
        String resetUrl = "https://BillMitra.com/reset-password?token=" + token;
        String subject = "Reset Your Password";
        String body = "Click the link below to reset your password:\n" + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}

