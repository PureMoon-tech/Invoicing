package com.example.invc_proj.controller;

import com.example.invc_proj.dto.AuthRequest;
import com.example.invc_proj.dto.AuthResponse;
import com.example.invc_proj.exceptions.InvalidPasswordLengthException;
import com.example.invc_proj.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

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
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
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



}




