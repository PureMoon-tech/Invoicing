package com.example.invc_proj.service;

import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ServicesRequestedRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoicePdfHandlerService {

    private final InvoiceRepo invoiceRepo;
    private final ServicesRequestedRepo servicesRequestedRepo;
    private final InvoicePdfService invoicePdfService;

    public byte[] getInvoicePdf(Invoice invoice) {
        List<ServicesRequested> services = servicesRequestedRepo.findByInvoiceId(invoice.getInvoice_id());
        return invoicePdfService.generateInvoicePdf(invoice, services);
    }
}
