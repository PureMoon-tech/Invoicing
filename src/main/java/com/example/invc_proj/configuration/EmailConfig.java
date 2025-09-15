package com.example.invc_proj.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class EmailConfig {

    @Bean("emailExecutor")
    public ExecutorService emailExecutor() {
        return new ThreadPoolExecutor(
                3, 10, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(50), // Queue up to 50 emails
                new ThreadPoolExecutor.CallerRunsPolicy() // Fallback if queue full
        );
    }
}