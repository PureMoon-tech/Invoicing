package com.example.invc_proj.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "app_roles")
@Data
public class AppRole {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_on")
    private Date createdOn;
}

