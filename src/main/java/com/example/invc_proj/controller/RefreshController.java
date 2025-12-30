package com.example.invc_proj.controller;


import com.example.invc_proj.dto.UserDTO;
import com.example.invc_proj.dto.UserPrincipalDTO;
import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
import com.example.invc_proj.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class RefreshController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserPrincipalDTO>> me(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
        {
            return ApiResponses.error(HttpStatus.UNAUTHORIZED,"No cookies found","/api/me");
        }

        // 2. Find the specific "access_token" cookie
        String token = Arrays.stream(cookies)
                .filter(cookie -> "ACCESS_TOKEN".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);

        if (token == null || !jwtUtil.validateToken(token))
        {
            return ApiResponses.error(HttpStatus.UNAUTHORIZED,"Invalid or missing token","/api/me");
        }

        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO();
        userPrincipalDTO = jwtUtil.extractUserPrincipal(token);
        return ApiResponses.ok(userPrincipalDTO);

    }

}
