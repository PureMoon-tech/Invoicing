package com.example.invc_proj.controller;



import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.service.InvoicePdfHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/invoices")
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class InvoicePDFController {

    private final InvoicePdfHandlerService pdfHandlerService;

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@RequestBody Invoice invoice)
    {
        byte[] pdfData = pdfHandlerService.getInvoicePdf(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoice_id() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
}

