package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesDropdownDTO {
    private int service_id;
    private String service_name;
    private String service_description;
    private BigDecimal min_price;
    private BigDecimal max_price;
}
