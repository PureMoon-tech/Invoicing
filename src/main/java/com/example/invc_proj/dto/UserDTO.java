package com.example.invc_proj.dto;

import com.example.invc_proj.model.AppRole;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String emailId;
    private Integer role_id;
    private String status;
    private String password;
    private String passwordSalt;


}
