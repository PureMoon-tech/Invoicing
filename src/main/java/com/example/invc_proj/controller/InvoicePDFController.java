package com.example.invc_proj.controller;



import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.service.InvoicePdfHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    @GetMapping("/invoices/pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody Invoice invoice) {
        try {
            // ✅ Fast async PDF generation with timeout
            byte[] pdf = pdfHandlerService.getInvoicePdfAsync(invoice)
                    .get(5, TimeUnit.SECONDS);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=invoice-" + invoice.getInvoice_id() + ".pdf")
                    .body(pdf);

        } catch (TimeoutException e) {
            return ResponseEntity.status(202)
                    .body("PDF generation taking longer than expected, please try again".getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error generating PDF".getBytes());
        }
    }

}

