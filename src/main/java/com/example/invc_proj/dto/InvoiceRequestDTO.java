package com.example.invc_proj.dto;

import com.example.invc_proj.model.Enum.InvoiceStatus;
import com.example.invc_proj.model.Enum.InvoiceType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class InvoiceRequestDTO
{
    private Integer client_Id;
    private Integer bank_id;
    private InvoiceStatus invoice_status;
    private InvoiceType invoice_type;
    private BigDecimal amount_paid;
    private List<ServiceCostRequest> serviceCostRequest;
}
