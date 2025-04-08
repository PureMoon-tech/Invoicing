package com.example.invc_proj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "app_roles")
@Data
public class AppRole {

    @Id
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_on")
    private Date createdOn;
}

