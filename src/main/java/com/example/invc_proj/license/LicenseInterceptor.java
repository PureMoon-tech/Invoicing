package com.example.invc_proj.license;


import com.example.invc_proj.exceptions.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
public class LicenseInterceptor implements HandlerInterceptor {

    @Autowired
    private LicenseManager licenseManager;  // Easy Spring bean injection

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // Access to handler - can check annotations, method info, etc.
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // Could check for custom annotations like @SkipLicenseCheck
        }

         LicenseVerifier.LicenseInfo license = licenseManager.getLicenseInfo();
        String method = request.getMethod();

        if (!license.isExpired()) {
            return true;
        }

        if ("GET".equalsIgnoreCase(method)) {
            response.addHeader("X-License-Status", "EXPIRED");
            response.addHeader("X-Read-Only-Mode", "true");
            return true;
        }

        // Block write operations
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .statusName("FORBIDDEN")
                .message("License expired. Only read operations are allowed.")
                .timestamp(LocalDateTime.now().toString())
                .path(request.getRequestURI())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        return false;
    }
}