package com.example.invc_proj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final long REFILL_INTERVAL_SECONDS = 60; // 1 minute

    public boolean isRequestAllowed(String key, int capacity) {
        String tokensKey = key + ":tokens";
        String timestampKey = key + ":timestamp";

        Long currentTime = System.currentTimeMillis() / 1000;
        String currentTokens = redisTemplate.opsForValue().get(tokensKey);
        String lastRefill = redisTemplate.opsForValue().get(timestampKey);

        double tokens;
        long lastRefillTime;

        if (currentTokens == null || lastRefill == null) {
            tokens = capacity;
            lastRefillTime = currentTime;
            redisTemplate.opsForValue().set(tokensKey, String.valueOf(tokens));
            redisTemplate.opsForValue().set(timestampKey, String.valueOf(lastRefillTime));
            redisTemplate.expire(tokensKey, REFILL_INTERVAL_SECONDS * 2, TimeUnit.SECONDS);
            redisTemplate.expire(timestampKey, REFILL_INTERVAL_SECONDS * 2, TimeUnit.SECONDS);
        } else {
            tokens = Double.parseDouble(currentTokens);
            lastRefillTime = Long.parseLong(lastRefill);
        }

        // Refill tokens based on elapsed time
        long elapsed = currentTime - lastRefillTime;
        double tokensToAdd = (double) elapsed / REFILL_INTERVAL_SECONDS * capacity;
        tokens = Math.min(tokens + tokensToAdd, capacity);
        lastRefillTime = currentTime;

        if (tokens < 1) {
            return false;
        }

        // Consume one token
        tokens -= 1;
        redisTemplate.opsForValue().set(tokensKey, String.valueOf(tokens));
        redisTemplate.opsForValue().set(timestampKey, String.valueOf(lastRefillTime));
        redisTemplate.expire(tokensKey, REFILL_INTERVAL_SECONDS * 2, TimeUnit.SECONDS);
        redisTemplate.expire(timestampKey, REFILL_INTERVAL_SECONDS * 2, TimeUnit.SECONDS);

        return true;
    }
}