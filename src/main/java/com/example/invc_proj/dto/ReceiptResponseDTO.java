package com.example.invc_proj.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReceiptResponseDTO {
    private int receiptId;
    private Long invoiceId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String paymentMode;
    private String reference;
    private String acknowledgedBy;
    private String remarks;
    private LocalDateTime insertedOn;
}

