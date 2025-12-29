package com.example.invc_proj.service;

import com.example.invc_proj.model.AuditLog;
import com.example.invc_proj.model.UserPrincipal;
import com.example.invc_proj.repository.AudtiLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Component
/*public class AuthenticationEventListener {
  /*  @Autowired
    private AudtiLogRepo auditLogRepository;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        logAuthEvent(event, "LOGIN");
    }

    @EventListener
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        logAuthEvent(event, "LOGIN_FAILED INCORRECT CREDENTIALS");
    }

    @EventListener
    public void onFailure(AuthenticationFailureCredentialsExpiredEvent event) {
        logAuthEvent(event, "LOGIN_FAILED CREDENTIALS EXPIRED");
    }

    @EventListener
    public void onLogout(LogoutSuccessEvent event) {
        logAuthEvent(event, "LOGOUT");
    }

    private void logAuthEvent(AbstractAuthenticationEvent event, String actionType) {
        Authentication auth = event.getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userPrincipal.getUsername();

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(username);
        auditLog.setAction(actionType);
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);
    }

   */

    @Component
    public class AuthenticationEventListener {

        @Autowired
        private AudtiLogRepo auditLogRepository;

        @EventListener
        @Async
        public void onSuccess(AuthenticationSuccessEvent event) {
            logAuthEvent(event.getAuthentication(), "LOGIN");
        }

        @EventListener
        @Async
        public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
            logAuthEvent(event.getAuthentication(), "LOGIN_FAILED INCORRECT CREDENTIALS");
        }

        @EventListener
        @Async
        public void onFailure(AuthenticationFailureCredentialsExpiredEvent event) {
            logAuthEvent(event.getAuthentication(), "LOGIN_FAILED CREDENTIALS EXPIRED");
        }

        @EventListener
        @Async
        public void onLogout(LogoutSuccessEvent event) {
            Authentication auth = event.getAuthentication();
            logAuthEvent(auth, "LOGOUT");
        }

        private void logAuthEvent(Authentication authentication, String actionType) {
            String username = "UNKNOWN";

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
                username = userPrincipal.getUsername();
            } else if (authentication != null) {
                // Sometimes, principal is just a string (e.g., username during login failure)
                username = String.valueOf(authentication.getPrincipal());
            }

            AuditLog auditLog = new AuditLog();
            auditLog.setUsername(username);
            auditLog.setAction(actionType);
            auditLog.setTimestamp(LocalDateTime.now());
            //System.out.println(username);
            //System.out.println(actionType);




            auditLogRepository.save(auditLog);
        }
    }

