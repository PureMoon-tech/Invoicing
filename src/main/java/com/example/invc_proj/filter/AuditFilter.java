package com.example.invc_proj.filter;

import com.example.invc_proj.model.AuditLog;
import com.example.invc_proj.repository.AuditLogRepo;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
@RequiredArgsConstructor
@Slf4j
public class AuditFilter extends OncePerRequestFilter {

    private final AuditLogRepo auditLogRepo;
    private final TaskExecutor taskExecutor;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            chain.doFilter(request, response);
            return;
        }

        String principal     = auth.getName();
        String correlationId = MDC.get("correlationId");
        String method        = request.getMethod();
        String uri           = request.getRequestURI();

        try {
            chain.doFilter(request, response);
        } finally {
            taskExecutor.execute(() -> {
                try {
                    AuditLog entry = new AuditLog();
                    entry.setCorrelationId(correlationId);
                    entry.setUsername(principal);
                    entry.setAction(method + " " + uri);
                    entry.setTimestamp(LocalDateTime.from(Instant.now()));
                    auditLogRepo.save(entry);
                } catch (Exception e) {
                    LoggerFactory.getLogger(getClass())
                            .error("Failed to save audit log correlationId={}", correlationId, e);
                }
            });
        }
    }
}