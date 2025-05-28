
package com.example.invc_proj.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.List;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "my-very-secret-key-my-very-secret-key"; // 256-bit key
    private final long EXPIRATION = 86400000; // 1 day

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }


    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Add roles claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

   /* public boolean isTokenValid(String token, String userDetailsUsername) {
        final String username = extractUsername(token);
        return username.equals(userDetailsUsername) && !isTokenExpired(token) &&!validTokenClaims(token);
    }*/

    public boolean isTokenValid(String token) {
        final String username = extractUsername(token);
        return  !isTokenExpired(token) && !validTokenClaims(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validTokenClaims(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}
