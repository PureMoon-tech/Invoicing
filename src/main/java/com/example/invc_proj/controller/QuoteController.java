package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ServiceCostRequest;
import com.example.invc_proj.model.Enum.QuoteStatus;
import com.example.invc_proj.model.Enum.QuoteType;
import com.example.invc_proj.model.Invoice;
import com.example.invc_proj.model.Quote;
import com.example.invc_proj.service.QuoteHandlerService;
import com.example.invc_proj.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController()
@RequestMapping("/quote")
@PreAuthorize("isAuthenticated()")

public class QuoteController {
    @Autowired
    private QuoteService service;

    @Autowired
    private QuoteHandlerService pdfHandlerService;

    @PostMapping("/generate-quote/{p_client_Id}/{p_quote_status}/{p_quote_type}")
    public ResponseEntity<Quote> generateInvoice (@PathVariable Integer p_client_Id,
                                                    @PathVariable QuoteStatus p_quote_status,
                                                    @PathVariable QuoteType p_quote_type,
                                                    @RequestBody List<ServiceCostRequest> serviceCostRequest)
    {
        System.out.println(p_client_Id);
        System.out.println(serviceCostRequest);
        Quote quote = service.generateQuote(p_client_Id,p_quote_status,p_quote_type,serviceCostRequest);
        return ResponseEntity.status(201).body(quote);
    }

    @GetMapping("/quotes/{p_quote_status}")
    public ResponseEntity<List<Quote>> getAllQuotes(@PathVariable QuoteStatus p_quote_status)
    {
        List<Quote> quotes = service.getAllQuotes(p_quote_status);
        return ResponseEntity.ok().body(quotes);
    }



    @GetMapping("/quotes/pdf")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody Quote quote) {
        try {
            // ✅ Fast async PDF generation with timeout
            byte[] pdf = pdfHandlerService.getQuotePdfAsync(quote)
                    .get(5, TimeUnit.SECONDS);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=quote-" + quote.getQuote_id() + ".pdf")
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
