package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    private String password;
    private String passwordSalt;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String status;


    @Column(name = "inserted_on", insertable = false, updatable = false)
    private Date insertedOn;

    // 🔐 Instead of storing role_id and role_name separately:
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private AppRole role;
}

