package com.example.invc_proj.dto;

import com.example.invc_proj.model.Enum.InvoiceStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class InvoiceUpdateDTO {
    private Long p_invoice_id;
    private InvoiceStatus p_invoice_status;
    private BigDecimal p_amount_paid;
}
