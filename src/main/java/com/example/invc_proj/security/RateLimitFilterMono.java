package com.example.invc_proj.security;

import com.example.invc_proj.service.RateLimiterServiceMono;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
@Component
public class RateLimitFilterMono extends OncePerRequestFilter {
    @Autowired
    private RateLimiterServiceMono rateLimiterService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String clientId = request.getRemoteAddr(); // Use IP, or extract user ID from headers/JWT
//        if (!rateLimiterService.isAllowed(clientId)) {
//            response.setStatus(429);
//            response.getWriter().write("Rate limit exceeded. Try again later.");
//            return;
//        }
//        filterChain.doFilter(request, response);
//    }

    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientId = request.getRemoteAddr();

        if (!rateLimiterService.isAllowed(clientId)) {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Create JSON response
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 429);
            errorResponse.put("error", "Too Many Requests");
            errorResponse.put("message", "Rate limit exceeded. Try again later.");
            errorResponse.put("path", request.getRequestURI());

            // Write JSON to response
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }


}

 */