
package com.example.invc_proj.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET = "my-very-secret-key-my-very-secret-key"; // 256-bit key
    private final long EXPIRATION = 86400000; // 1 day

    @Value("${jwt.expiration.access:900000}") // 15 minutes
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.expiration.refresh:86400000}") // 15 minutes
    private long REFRESH_TOKEN_EXPIRATION;

    //private Key getSigningKey() {return Keys.hmacShaKeyFor(SECRET.getBytes());}
    // Helper method to get the signing key from the secret string
    private Key getSigningKey() {
        // Ensure the secret is long enough for HS256 (32 bytes / 256 bits)
        if (SECRET.length() < 32)
        {
            throw new RuntimeException("WARNING: JWT secret key is too short for HS256. It should be at least 32 characters.");
        }
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a new JWT access token.
     //* @param username The subject of the token (e.g., user's email or ID).
     //* @param roles A list of roles/authorities for the user.
     * @return The generated JWT string.
     */
   public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Add roles claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT.
     //* @param token The JWT string.
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT using a claims resolver function.
    // * @param token The JWT string.
    // * @param claimsResolver A function to resolve the desired claim from the Claims object.
     //* @param <T> The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from a JWT.
     * @param token The JWT string.
     * @return The Claims object.
     * @throws ExpiredJwtException if the token is expired.
     * @throws SignatureException if the token signature is invalid.
     * @throws MalformedJwtException if the token is not a valid JWT.
     * @throws UnsupportedJwtException if the JWT is not supported.
     * @throws IllegalArgumentException if the token is null or empty.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Use the secure signing key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

   /* public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }*/

   /* public boolean isTokenValid(String token, String userDetailsUsername) {
        final String username = extractUsername(token);
        return username.equals(userDetailsUsername) && !isTokenExpired(token) &&!validTokenClaims(token);
    }*/



    /**
     * Validates a JWT token.
     * This version checks expiration, signature, and optionally, if the token's subject matches
     * the username loaded from UserDetailsService.
     *
     * @param token The JWT string.
     * @param userDetailsUsername The username from the UserDetails loaded by Spring Security.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, String userDetailsUsername) {
        try {
            final String usernameInToken = extractUsername(token);
            // 1. Check if the username in the token matches the authenticated user's username
            // 2. Check if the token is not expired
            // 3. Implicitly checks signature and other parsing errors via extractAllClaims
            return (usernameInToken.equals(userDetailsUsername)) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if the JWT token has expired.
     * @param token The JWT string.
     * @return true if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        // Use extractClaim to safely get the expiration date
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

   /* private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }*/

    public boolean isTokenValid(String token) {
        final String username = extractUsername(token);
        return  !isTokenExpired(token) && !validTokenClaims(token);
    }



    public boolean validTokenClaims(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
            return false;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
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
