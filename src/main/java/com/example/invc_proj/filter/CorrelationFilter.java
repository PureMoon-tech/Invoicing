package com.example.invc_proj.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationFilter extends OncePerRequestFilter {

    public static final String CORRELATION_HEADER = "X-Correlation-Id";
    public static final String MDC_CORRELATION    = "correlationId";
    public static final String MDC_REQUEST_ID     = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        String correlationId = resolveCorrelationId(req);
        String requestId     = UUID.randomUUID().toString();

        MDC.put(MDC_CORRELATION, correlationId);
        MDC.put(MDC_REQUEST_ID,  requestId);
        MDC.put("method",        req.getMethod());
        MDC.put("uri",           req.getRequestURI());

        res.setHeader(CORRELATION_HEADER, correlationId);
        res.setHeader("X-Request-Id",     requestId);

        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();   // ← always, even on exception
        }
    }

    private String resolveCorrelationId(HttpServletRequest req) {
        String incoming = req.getHeader(CORRELATION_HEADER);
        return StringUtils.hasText(incoming)
                ? sanitize(incoming)
                : UUID.randomUUID().toString();
    }

    private String sanitize(String v) {
        return v.replaceAll("[^a-zA-Z0-9\\-_]", "")
                .substring(0, Math.min(v.length(), 64));
    }
}