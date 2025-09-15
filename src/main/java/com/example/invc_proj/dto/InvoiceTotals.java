package com.example.invc_proj.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@NoArgsConstructor
public class InvoiceTotals {

    BigDecimal totalServiceCost;
    BigDecimal totalGstValue;
    BigDecimal totalTdsValue;
}
