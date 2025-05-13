package com.example.invc_proj.dto;

import lombok.Data;

@Data
public class PasswordUpdateRequestDTO {

        private String oldPassword;
        private String newPassword;

    public PasswordUpdateRequestDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
