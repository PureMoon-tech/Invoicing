package com.example.invc_proj.controller;



import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.ServicesRequested;
import com.example.invc_proj.repository.InvoiceRepo;
import com.example.invc_proj.repository.ServicesRequestedRepo;
import com.example.invc_proj.service.InvoicePdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@PreAuthorize("isAuthenticated()")
public class InvoicePDFController {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private ServicesRequestedRepo servicesRequestedRepo;

    @Autowired
    private InvoicePdfService invoicePdfService;

    /*end point to create pdf of generated invoice*/
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@RequestBody Invoice invoice) {

        // Fetch Services mapped to the invoice
        List<ServicesRequested> services = servicesRequestedRepo.findByInvoiceId(invoice.getInvoice_id());

        // Generate PDF
        byte[] pdfData = invoicePdfService.generateInvoicePdf(invoice, services);

        // Prepare Response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoice_id() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
}
