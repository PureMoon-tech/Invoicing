package com.example.invc_proj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RateLimiterServiceMono {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_REQUESTS = 10; // Max requests per window
    private static final long WINDOW_SECONDS = 60; // 1-minute window

    public boolean isAllowed(String clientId) {
        String key = "rate_limit:" + clientId;
        long now = Instant.now().getEpochSecond();
        long windowStart = now - WINDOW_SECONDS;

        // Remove requests older than the window
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStart);

        // Count requests in the current window
        Long requestCount = redisTemplate.opsForZSet().zCard(key);
        if (requestCount == null) {
            requestCount = 0L;
        }

        // Check if under limit
        if (requestCount >= MAX_REQUESTS) {
            return false;
        }

        // Add current request timestamp
        redisTemplate.opsForZSet().add(key, now, now);
        // Set expiration to clean up old keys
        redisTemplate.expire(key, java.time.Duration.ofSeconds(WINDOW_SECONDS));
        return true;
    }
}