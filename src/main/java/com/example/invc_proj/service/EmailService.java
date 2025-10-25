package com.example.invc_proj.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    private final ExecutorService emailExecutor =  Executors.newFixedThreadPool(5);

    public CompletableFuture<Void> sendWelcomeEmail(String toEmail, String userName) {

        return CompletableFuture.runAsync(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("Welcome to BillMitra!");
                message.setText("Hi " + userName + ",\n\nWelcome to BillMitra! We're excited to have you on board." +
                        "\n\nBest Regards," +
                        "\nPureMoon-Tech");
                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, emailExecutor);

    }

    public CompletableFuture<Void> sendResetEmail(String email, String token) {

        return CompletableFuture.runAsync(() -> {
            try {
                String resetUrl = "https://BillMitra.com/reset-password?token=" + token;
                String subject = "Reset Your Password";
                String body = "Click the link below to reset your password:\n" + resetUrl;

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(email);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, emailExecutor);

    }
}

