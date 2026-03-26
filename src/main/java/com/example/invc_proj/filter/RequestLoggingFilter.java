package com.example.invc_proj.filter;

import com.example.invc_proj.model.ActivityLog;
import com.example.invc_proj.repository.ActivityLogRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@RequiredArgsConstructor
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final ActivityLogRepo      activityLogRepo;
    private final TaskExecutor         taskExecutor;
    private final SensitiveFieldMasker masker;

    private static final int MAX_BODY_LOG_BYTES = 512_000;  // 512KB — JVM heap protection

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (shouldSkip(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper  req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();

        try {
            chain.doFilter(req, res);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String correlationId = MDC.get("correlationId");
            String requestId     = MDC.get("requestId");
            String principal     = Optional
                    .ofNullable(SecurityContextHolder.getContext().getAuthentication())
                    .filter(a -> a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken))
                    .map(Principal::getName)
                    .orElse("anonymous");

            int    status  = res.getStatus();
            String method  = req.getMethod();
            String uri     = req.getRequestURI();
            String ct      = req.getContentType();
            byte[] reqBody = req.getContentAsByteArray();
            byte[] resBody = res.getContentAsByteArray();

            log.info("HTTP request completed method={} uri={} status={} " +
                            "duration_ms={} principal={} correlationId={}",
                    method, uri, status, duration, principal, correlationId);


            res.copyBodyToResponse();

            taskExecutor.execute(() -> saveActivityLog(
                    correlationId, requestId, principal,
                    method, uri, status, duration,
                    reqBody, resBody, ct
            ));
        }
    }

    private void saveActivityLog(String correlationId, String requestId,
                                 String principal, String method, String uri,
                                 int status, long duration,
                                 byte[] reqBody, byte[] resBody,
                                 String contentType) {
        try {
            String maskedRequest  = masker.mask(captureBody(reqBody,  correlationId, "request"));
            String maskedResponse = masker.mask(captureBody(resBody,  correlationId, "response"));

            ActivityLog entry = new ActivityLog();
            entry.setCorrelationId(correlationId);
            entry.setRequestId(requestId);
            entry.setUserName(principal);
            entry.setMethod(method);
            entry.setUri(uri);
            entry.setStatus(status);
            entry.setDurationMs(duration);
            entry.setTimestamp(Instant.now());
            entry.setRequestBody(maskedRequest);
            entry.setResponseBody(maskedResponse);

            activityLogRepo.save(entry);

        } catch (Exception e) {
            log.error("Failed to save activity log correlationId={} uri={} error={}",
                    correlationId, uri, e.getMessage(), e);
        }
    }

    /**
     * Captures body bytes as a String.
     * Guard is purely for JVM heap protection — Postgres TEXT has no size limit.
     * correlationId passed explicitly because MDC is cleared before this runs.
     */
    private String captureBody(byte[] content, String correlationId, String type) {
        if (content == null || content.length == 0) return null;

        if (content.length > MAX_BODY_LOG_BYTES) {
            log.warn("Body exceeds capture limit, skipping " +
                            "type={} correlationId={} actual_kb={} max_kb={}",
                    type, correlationId,
                    content.length / 1024,
                    MAX_BODY_LOG_BYTES / 1024);
            return String.format("[body not captured — %dKB exceeds %dKB limit]",
                    content.length / 1024, MAX_BODY_LOG_BYTES / 1024);
        }

        return new String(content, StandardCharsets.UTF_8);
    }

    private boolean shouldSkip(String path) {
        return path.contains("/css/")
                || path.contains("/js/")
                || path.contains("/images/")
                || path.contains("/actuator/health")
                || path.contains("/favicon.ico")
                || path.contains("/error");
    }
}