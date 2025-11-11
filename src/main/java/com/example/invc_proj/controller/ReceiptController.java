package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ReceiptRequestDTO;
import com.example.invc_proj.dto.ReceiptResponseDTO;
import com.example.invc_proj.exceptions.ApiResponse;
import com.example.invc_proj.exceptions.ApiResponses;
import com.example.invc_proj.service.ReceiptService;
import com.example.invc_proj.service.ReceiptServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService Service;

    @PostMapping("/generate-receipt")
    public ResponseEntity<ApiResponse<ReceiptResponseDTO>> createReceipt(@RequestBody ReceiptRequestDTO request) {
        return ApiResponses.ok(Service.addReceipt(request));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<ApiResponse<List<ReceiptResponseDTO>>> getReceiptsByInvoiceId(@PathVariable int invoiceId) {
        return ApiResponses.ok(Service.getReceiptsByInvoiceId(invoiceId));
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ApiResponse<ReceiptResponseDTO>> getReceiptById(@PathVariable int receiptId) {
        return ApiResponses.ok(Service.getReceiptById(receiptId));
    }
}
