package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDropownDTO {
        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private String emailId;
        private Integer role_id;
        private String status;
}
