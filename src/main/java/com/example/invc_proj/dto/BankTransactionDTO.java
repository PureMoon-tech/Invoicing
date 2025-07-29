package com.example.invc_proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public  class BankTransactionDTO
{
    private LocalDate date;
    private BigDecimal amount;
    private String reference;
    private String payerId;
    private String description;

    public BankTransactionDTO(LocalDate date, BigDecimal amount, String reference, String payerId,
                              String description)
    {
        this.date = date;
        this.amount = amount;
        this.reference = reference;
        this.payerId = payerId;
        this.description = description;
    }

}