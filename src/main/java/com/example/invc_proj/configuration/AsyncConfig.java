package com.example.invc_proj.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);    // Minimum threads
        executor.setMaxPoolSize(10);    // Maximum threads
        executor.setQueueCapacity(25);  // Queue capacity
        executor.setThreadNamePrefix("MyAsync-");
        executor.initialize();
        return executor;
    }
}
