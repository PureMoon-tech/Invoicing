package com.example.invc_proj.dto;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class ReceiptRequestDTO {
    private long invoiceId;
    private Long amount;
    private LocalDateTime paymentDate;
    private String paymentMode;
    private String reference;
    private String remarks;
    private int acknowledgedByUserId;
}
