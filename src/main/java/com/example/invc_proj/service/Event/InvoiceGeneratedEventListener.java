package com.example.invc_proj.service.Event;

import com.example.invc_proj.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class InvoiceGeneratedEventListener {

    private final ReceiptService receiptService;
    // Listener for invoice generation event
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleInvoiceGeneratedEvent(InvoiceGeneratedEvent event) {
        receiptService.generateAndSave(event.getInvoice(), event.getPaymentAmount());
    }

}
