package com.example.invc_proj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class APP_MEMBERSHIP {
    @Id
    private int app_id;

    private int user_id;
    private String password;
    private String password_salt;
    private String mobile_pin;
    private String email;
    private Boolean is_aprroved;
    private Boolean is_active;
    private Date created_date;
    private Date last_login_date;
    private Date last_password_modified_date;
    private Date last_locked_out_date;
    private int failed_password_attempt_count;


}
