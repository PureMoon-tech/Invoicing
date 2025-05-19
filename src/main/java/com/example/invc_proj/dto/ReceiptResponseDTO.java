package com.example.invc_proj.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReceiptResponseDTO {
    private int receiptId;
    private int invoiceId;
    private long amount;
    private LocalDateTime paymentDate;
    private String paymentMode;
    private String reference;
    private String acknowledgedBy;
    private String remarks;
    private LocalDateTime insertedOn;
}

