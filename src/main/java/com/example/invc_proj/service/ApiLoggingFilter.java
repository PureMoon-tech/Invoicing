package com.example.invc_proj.service;


import com.example.invc_proj.model.ActivityLog;
import com.example.invc_proj.model.AuditLog;
import com.example.invc_proj.repository.ActvityLogRepo;
import com.example.invc_proj.repository.AudtiLogRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class ApiLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);

    @Autowired
    private ActvityLogRepo activityLogRepository;

    @Autowired
    private AudtiLogRepo auditLogRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip static resources
        if (shouldSkipLogging(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Wrap request to allow reading the body multiple times
        ContentCachingRequestWrapper requestWrapper =
                new ContentCachingRequestWrapper(request);

        // Wrap response to capture the response body
        ContentCachingResponseWrapper responseWrapper =
                new ContentCachingResponseWrapper(response);

        try {
            // Process the request through the filter chain
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            // Capture endpoint information
            String endpoint = requestWrapper.getRequestURI();

            // Log activity (for all requests)
            try {
                logActivity(requestWrapper, responseWrapper, endpoint);
            } catch (Exception e) {
                logger.error("Failed to log activity", e);
            }

            // Log audit (for authenticated users)
            try {
                logAudit(requestWrapper, endpoint);
            } catch (Exception e) {
                logger.error("Failed to log audit", e);
            }

            // Copy content back to the original response
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logActivity(ContentCachingRequestWrapper request,
                             ContentCachingResponseWrapper response,
                             String endpoint) {

        taskExecutor.execute(() -> {
            try {
                // Create activity log
                ActivityLog activityLog = new ActivityLog();
                activityLog.setEndpoint(endpoint);
                activityLog.setInsertedOn(LocalDateTime.now());

                // Capture request parameters
                String requestParams = captureRequestParams(request);
                activityLog.setRequestParams(requestParams);

                // Capture response body summary
                String responseBody = captureResponseBody(response);
                activityLog.setResponseSummary(responseBody);

                // Save to database
                activityLogRepository.save(activityLog);
            } catch (Exception e) {
                logger.error("Error saving activity log", e);
            }
        });

    }

    private void logAudit(HttpServletRequest request, String endpoint) {

        taskExecutor.execute(() -> {
            try {
                // Only create audit logs for authenticated users
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() &&
                        !(auth instanceof AnonymousAuthenticationToken)) {

                    String username = auth.getName();
                    String action = request.getMethod() + " " + endpoint;

                    AuditLog auditLog = new AuditLog();
                    auditLog.setUsername(username);
                    auditLog.setAction(action);
                    auditLog.setTimestamp(LocalDateTime.now());

                    // Save to database
                    auditLogRepository.save(auditLog);
                }
            } catch (Exception e) {
                logger.error("Error saving audit log", e);
            }
        });

    }

    private String captureRequestParams(ContentCachingRequestWrapper request) {
        StringBuilder params = new StringBuilder();


        String queryString = request.getQueryString();
        if (queryString != null) {
            params.append("Query: ").append(queryString).append("; ");
        }


        params.append("Method: ").append(request.getMethod()).append("; ");


        if (isContentTypeJson(request.getContentType()) &&
                (request.getMethod().equals("POST") ||
                        request.getMethod().equals("PUT") ||
                        request.getMethod().equals("PATCH"))) {

            byte[] content = request.getContentAsByteArray();
            if (content.length > 0) {
                String body = new String(content, StandardCharsets.UTF_8);
                // Truncate if too long
                if (body.length() > 3900) {
                    body = body.substring(0, 3900) + "...";
                }
                params.append("Body: ").append(body);
            }
        } else {
            // For form submissions, get parameter names and values
            Enumeration<String> paramNames = request.getParameterNames();
            if (paramNames.hasMoreElements()) {
                params.append("Form: ");
                while (paramNames.hasMoreElements()) {
                    String name = paramNames.nextElement();
                    String value = request.getParameter(name);

                    // Mask sensitive data
                    if (name.toLowerCase().contains("password") ||
                            name.toLowerCase().contains("token") ||
                            name.toLowerCase().contains("secret")) {
                        value = "*****";
                    }

                    params.append(name).append("=").append(value).append(", ");
                }
            }
        }

        return params.toString();
    }

    private String captureResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String body = new String(content, StandardCharsets.UTF_8);
            // Truncate if too long
            if (body.length() > 3900) {
                body = body.substring(0, 3900) + "...";
            }
            return "Status: " + response.getStatus() + "; Body: " + body;
        } else {
            return "Status: " + response.getStatus() + "; No body";
        }
    }

    private boolean isContentTypeJson(String contentType) {
        return contentType != null && contentType.contains("application/json");
    }

    private boolean shouldSkipLogging(String path) {
        return path.contains("/css/") ||
                path.contains("/js/") ||
                path.contains("/images/") ||
                path.contains("/favicon.ico") ||
                path.contains("/error");
    }
}