package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;

//@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Login
    {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", unique = true, nullable = false)
        private int user_id;
        private String user_password;
        private String password_salt;
        private String password;
    }
