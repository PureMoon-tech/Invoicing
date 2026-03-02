package com.example.invc_proj.dto;

import com.example.invc_proj.model.Quote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServicesQuotedDTO {
    private int service_id;
    private BigDecimal service_cost;
    private int tds_rate;
    private int gst_rate;
    private BigDecimal tds_value;
    private BigDecimal gst_value;
    private BigDecimal service_total;

}
