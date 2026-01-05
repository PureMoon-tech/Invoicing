package com.example.invc_proj.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder encoder;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:token:";
    private static final long REFRESH_TOKEN_TTL_HOURS = 24;

    /**
     * Create & store refresh token in Redis (hashed)
     */
    public String createRefreshToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate raw JWT refresh token
        String rawToken = jwtUtil.generateRefreshToken(username);
       // System.out.println("rawToken"+rawToken);
        // Hash it before storing
        //String hashedToken = encoder.encode(rawToken);
        //System.out.println("hashedToken"+hashedToken);
        // Store in Redis: key = refresh:token:<hashed>, value = username
        String redisKey = REFRESH_TOKEN_PREFIX + rawToken;
        redisTemplate.opsForValue().set(redisKey, username,
                REFRESH_TOKEN_TTL_HOURS, TimeUnit.HOURS);

        // Optional: index by user to allow revocation
        redisTemplate.opsForValue().set("refresh:user:" + username, rawToken,
                REFRESH_TOKEN_TTL_HOURS, TimeUnit.HOURS);
        //System.out.println("hashedToken"+hashedToken);
        return rawToken; // Return raw token to client
    }

    /**
     * Validate refresh token
     */
    public Optional<String> validateAndGetUsername(String rawToken) {
        //String hashedToken = encoder.encode(rawToken);
        String redisKey = REFRESH_TOKEN_PREFIX + rawToken;

        String username = redisTemplate.opsForValue().get(redisKey);
       // System.out.println("username"+username);
        if (username != null && jwtUtil.validateToken(rawToken)) {
            return Optional.of(username);
        }
        return Optional.empty();
    }

    /**
     * Revoke token (logout)
     */
    public void revokeToken(String rawToken) {
        //String hashedToken = encoder.encode(rawToken);
        String tokenKey = REFRESH_TOKEN_PREFIX + rawToken;
        String username = redisTemplate.opsForValue().get(tokenKey);
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }

        if (username != null) {
            System.out.println(tokenKey);
            redisTemplate.delete(tokenKey);
            redisTemplate.delete("refresh:user:" + username);
        }
    }

    /**
     * Revoke all tokens for user (e.g., password change)
     */
    public void revokeAllUserTokens(String username) {
        String userKey = "refresh:user:" + username;
        String hashedToken = redisTemplate.opsForValue().get(userKey);
        if (hashedToken != null) {
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + hashedToken);
            redisTemplate.delete(userKey);
        }
    }

    /**
     * Rotate: revoke old, issue new
     */
    public String rotateRefreshToken(String oldRawToken, String username) {
        revokeToken(oldRawToken);
        return createRefreshToken(username);
    }
}