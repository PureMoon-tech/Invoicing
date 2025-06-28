package com.example.invc_proj.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Arrays;

@Configuration
public class DummyMailSenderConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl() {
            @Override
            public void send(SimpleMailMessage message) {
                System.out.println("\n[Mock Email Sent]");
                System.out.println("To: " + Arrays.toString(message.getTo()));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("Text: " + message.getText());
            }
        };
    }
}
