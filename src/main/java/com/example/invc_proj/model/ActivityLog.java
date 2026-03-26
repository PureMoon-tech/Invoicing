package com.example.invc_proj.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String correlationId;
    private String requestId;
    private String userName;
    private String method;
    private String uri;
    private int    status;
    private long   durationMs;
    private Instant timestamp;
    @Column(columnDefinition = "TEXT")
    private String requestBody;
    @Column(columnDefinition = "TEXT")
    private String responseBody;
}