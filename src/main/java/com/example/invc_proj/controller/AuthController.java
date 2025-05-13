package com.example.invc_proj.controller;

import com.example.invc_proj.dto.AuthRequest;
import com.example.invc_proj.dto.AuthResponse;
import com.example.invc_proj.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
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
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest request) {
    System.out.println(request);



    // Step 1: Validate the license before authenticating the user
   /* if (!licenseManager.isLicenseValid()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("License validation failed: Expired or user limit exceeded.");
    }
    */

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

}




