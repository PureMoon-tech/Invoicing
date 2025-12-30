package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPrincipalDTO {
    private String userName;
    private int userId;
    private String userEmail;
    private List<String> roles;
}
