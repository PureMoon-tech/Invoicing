package com.example.invc_proj.security;

import com.example.invc_proj.configuration.EndPointTypeIdentifier;
import com.example.invc_proj.exceptions.ApiError;
import com.example.invc_proj.exceptions.RateLimiterException;
import com.example.invc_proj.service.RateLimiterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
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

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private EndPointTypeIdentifier endpointTypeIdentifier;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = getClientIp(request);
        String endpointType = endpointTypeIdentifier.identify(request);
        int limit = endpointTypeIdentifier.getLimitByType(endpointType);
        String key = String.format("rate_limit:%s:%s", clientIp, endpointType);

        if (!rateLimiterService.isRequestAllowed(key, limit))
        {
            response.setStatus(429);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ApiError apiError = new ApiError(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            ("Too Many Requests - limit exceeded. Try after some time."),
            ("Too Many Requests"),
            (request.getRequestURI())
            );
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), apiError);
            return;
            //throw new RateLimiterException("Too Many Requests-limit exceeded Try after some time");
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}