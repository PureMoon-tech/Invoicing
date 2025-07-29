package com.example.invc_proj.controller;

import com.example.invc_proj.dto.ReceiptRequestDTO;
import com.example.invc_proj.dto.ReceiptResponseDTO;
import com.example.invc_proj.service.ReceiptService;
import com.example.invc_proj.service.ReceiptServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

    @Autowired
    private ReceiptService Service;

    @PostMapping("/generate-receipt")
    public ResponseEntity<ReceiptResponseDTO> createReceipt(@RequestBody ReceiptRequestDTO request) {
        return ResponseEntity.ok(Service.addReceipt(request));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<ReceiptResponseDTO>> getReceiptsByInvoiceId(@PathVariable int invoiceId) {
        return ResponseEntity.ok(Service.getReceiptsByInvoiceId(invoiceId));
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<ReceiptResponseDTO> getReceiptById(@PathVariable int receiptId) {
        return ResponseEntity.ok(Service.getReceiptById(receiptId));
    }
}
