package com.example.invc_proj.configuration;


import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class EndPointTypeIdentifier {

    public static final String AUTH = "AUTH";
    public static final String RESOURCE = "RESOURCE";
    public static final String NON_RESOURCE = "NON_RESOURCE";

    public String identify(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.matches("/auth/.*")) {
            return AUTH;
        } else if (path.matches("/api/resources/.*")) {
            return RESOURCE;
        } else {
            return NON_RESOURCE;
        }
    }

    public int getLimitByType(String type) {
        switch (type) {
            case AUTH:
                return 3;
            case RESOURCE:
                return 3;
            case NON_RESOURCE:
                return 50;
            default:
                return 50; // Default to non-resource limit
        }
    }
}