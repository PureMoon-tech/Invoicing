package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;


@Entity
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private LocalDateTime expiryDate;
}

