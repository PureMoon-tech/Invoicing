package com.example.invc_proj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class APP_ROLES {

    @Id
    @Column(name = "role_id", unique = true, nullable = false)
    private int role_id;

    private String role_name;
    private String description;
    private Boolean is_active;
    private Date created_on;
}
