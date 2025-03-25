package com.example.invc_proj.dto;

import lombok.*;
@Data
public class ClientDropdownDTO {

    private int client_id;
    private String client_name;
    private String client_type;

    public ClientDropdownDTO(int client_id, String client_name, String client_type) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_type = client_type;
    }


}
