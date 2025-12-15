package com.example.invc_proj.controller;

import com.example.invc_proj.dto.AuthRequest;
import com.example.invc_proj.dto.AuthResponse;
import com.example.invc_proj.dto.RefreshRequest;
import com.example.invc_proj.exceptions.InvalidPasswordLengthException;
import com.example.invc_proj.exceptions.InvalidRefreshTokenException;
import com.example.invc_proj.model.AuditLog;
import com.example.invc_proj.model.UserPrincipal;
import com.example.invc_proj.repository.AudtiLogRepo;
import com.example.invc_proj.security.CustomUserDetailsService;
import com.example.invc_proj.security.JwtUtil;
import com.example.invc_proj.security.RefreshTokenService;
import com.example.invc_proj.service.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")

    public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AudtiLogRepo audtiLogRepo;

    private final RefreshTokenService refreshTokenService;

    private final CustomUserDetailsService CustomUserDetailsService;

    //@Autowired
   // private LicenseManager licenseManager;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request)
//    {
//        System.out.println(request);
//        Authentication authentication = authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        List<String> roles = userDetails.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        String token = jwtUtil.generateToken(request.getUsername(),roles);
//
//        return ResponseEntity.ok(new AuthResponse(token));
//    }
//
/*
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    System.out.println(request);



    // Step 1: Validate the license before authenticating the user
   //if (!licenseManager.isLicenseValid()) {
     //   return ResponseEntity.status(HttpStatus.FORBIDDEN)
       //         .body("License validation failed: Expired or user limit exceeded.");
    //}


    Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
    );
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    List<String> roles = userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    String token = jwtUtil.generateToken(request.getUsername(), roles);

    // Step 2: Increment active user count after successful authentication
   // licenseManager.incrementActiveUsers();

    return ResponseEntity.ok(new AuthResponse(token));
}
*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
        System.out.println(request);



        // Step 1: Validate the license before authenticating the user
       /* if (!licenseManager.isLicenseValid()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("License validation failed: Expired or user limit exceeded.");
        }
        */
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            System.out.println(userPrincipal);
            List<String> roles = userPrincipal.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtUtil.generateToken(userPrincipal.getUsername(), userPrincipal.getUserId(),userPrincipal.getEmailId(),roles);
            System.out.println(token);
            String refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
            System.out.println(refreshToken);
            // Step 2: Increment active user count after successful authentication
            // licenseManager.incrementActiveUsers();

            //return ResponseEntity.ok(new AuthResponse(token,refreshToken));
            ResponseCookie accessTokenCookie = ResponseCookie
                    .from("ACCESS_TOKEN", token)
                    .httpOnly(true)
                    .secure(false)           // true in prod (HTTPS)
                    .sameSite("Lax")        // or "Strict"
                    .path("/")
                    .maxAge(Duration.ofMinutes(15))
                    .build();

            ResponseCookie refreshTokenCookie = ResponseCookie
                    .from("REFRESH_TOKEN", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Strict")
                    .path("/auth/refresh")
                    .maxAge(Duration.ofDays(1))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                    .body(new AuthResponse(token,"Login successful"));

        }
        catch (BadCredentialsException ex)
        {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "status", 401,
                            "error", "Invalid username or password",
                            "timestamp", LocalDateTime.now().toString()

                    ));
        }
        catch (Exception ex)
        {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", 500,
                            "error", "An unexpected error occurred",
                            "timestamp", LocalDateTime.now().toString()
                    ));
        }
    }

   /* @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = tokenHeader.substring(7); // Remove "Bearer " prefix
            String username = jwtUtil.extractUsername(token);

            // if (username != null && jwtUtil.isTokenValid(token,userName))
            if (username != null && jwtUtil.isTokenValid(token)){
                return ResponseEntity.ok("Token is valid");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                //throw new InvalidPasswordLengthException("Password must be at least 8 characters long");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed: " + e.getMessage());
        }
        */


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            AuditLog log = new AuditLog();
            log.setUsername(username);
            log.setAction("LOGOUT");
            log.setTimestamp(LocalDateTime.now());
            audtiLogRepo.save(log);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String tokenHeader) {
        try {
            if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", 401, "message", "Missing or invalid Authorization header"));
            }

            String token = tokenHeader.substring(7); // Remove "Bearer " prefix
            String username = jwtUtil.extractUsername(token); // May throw exception if tampered

            if (username != null && jwtUtil.isTokenValid(token)) {
                return ResponseEntity.ok(Map.of("status", 200, "message", "Token is valid", "username", username));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("status", 401, "message", "Invalid token"));
            }
        } catch (io.jsonwebtoken.JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", 401, "message", "Invalid or malformed token", "error", e.getClass().getSimpleName()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", 500, "message", "Token validation failed", "error", e.getMessage()));
        }
    }

    @Autowired
    private PasswordResetService resetService;

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        resetService.createAndSendResetToken(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        resetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully");
    }

    @PostMapping("/refreshold")
    public ResponseEntity<AuthResponse> refreshold(@RequestBody RefreshRequest req)
    {
        String username = refreshTokenService.validateAndGetUsername(req.getRefreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid/expired refresh token"));
        //System.out.println("username"+username);
        UserPrincipal user = CustomUserDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtUtil.generateToken(username,user.getUserId(),user.getEmailId(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        //System.out.println("newAccessToken"+newAccessToken);
        //System.out.println("req.getRefreshToken()"+req.getRefreshToken());
        String newRefreshToken = refreshTokenService.rotateRefreshToken(req.getRefreshToken(), username);
        //System.out.println("newRefreshToken"+newRefreshToken);
        //return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
        ResponseCookie accessTokenCookie = ResponseCookie
                .from("ACCESS_TOKEN", newAccessToken)
                .httpOnly(true)
                .secure(false)           // true in prod (HTTPS)
                .sameSite("Lax")        // or "Strict"
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("REFRESH_TOKEN", newRefreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AuthResponse(newAccessToken,"Login successful"));

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = jwtUtil.extractRefreshToken(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("access_token","Refresh token missing"));
        }

        String username = refreshTokenService
                .validateAndGetUsername(refreshToken)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("Invalid/expired refresh token"));

        UserPrincipal user =
                CustomUserDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.generateToken(
                username,
                user.getUserId(),
                user.getEmailId(),
                user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );

        String newRefreshToken =
                refreshTokenService.rotateRefreshToken(refreshToken, username);

        ResponseCookie accessTokenCookie = ResponseCookie
                .from("ACCESS_TOKEN", newAccessToken)
                .httpOnly(true)
                .secure(false)              // true in prod (HTTPS)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie
                .from("REFRESH_TOKEN", newRefreshToken)
                .httpOnly(true)
                .secure(false)              // true in prod
                .sameSite("Strict")
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AuthResponse(newAccessToken,"Token refreshed"));
    }

   /* @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest req) {
        refreshTokenService.revokeToken(req.getRefreshToken());
        return ResponseEntity.ok("Logged out");
    }
*/

}




