package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInvoiceDTO {
    private String client_name;
    private String address;
    private String pincode;
}

