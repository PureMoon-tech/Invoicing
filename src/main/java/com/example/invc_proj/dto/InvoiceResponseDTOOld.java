package com.example.invc_proj.dto;

import com.example.invc_proj.model.Enum.InvoiceStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class InvoiceResponseDTOOld {
    private Long invoiceId;
    private String invoiceNumber;
    private int clientId;
    private int userId;
    private Date invoiceGeneratedDate;
    private BigDecimal total;
    private InvoiceStatus status;
    private int bankId;
    private BigDecimal amountPaid;

}
