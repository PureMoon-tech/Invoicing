package com.example.invc_proj.service.Event;

import com.example.invc_proj.model.Invoice;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceGeneratedEvent {
    private final Invoice invoice;
    private final BigDecimal paymentAmount;

    public InvoiceGeneratedEvent(Invoice invoice, BigDecimal paymentAmount) {
        this.invoice = invoice;
        this.paymentAmount = paymentAmount;
    }

}